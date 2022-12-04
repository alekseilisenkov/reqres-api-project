package com.alexlis.test.pet;

import com.alexlis.dto.pet.request.AddNewPetToStoreRequest;
import com.alexlis.dto.pet.request.UpdateAnExistingPetRequest;
import com.alexlis.dto.pet.response.PetModelResponse;
import com.alexlis.helpers.BodyGenerator;
import com.alexlis.helpers.FakerData;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Epic("Add a new service.pet to the store")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateAnExistingPet extends TestBase {

    @Test
    @Owner(value = "Lisenkov Alexey")
    @DisplayName("Add a new service.pet to the store")
    public void testAddNewPetToStore() {
        Allure.step("Step 1: Add a new service.pet to the store", () -> {
            AddNewPetToStoreRequest addNewPetToStore = BodyGenerator.getAddingNewPet()
                    .withId(Integer.parseInt(FakerData.getRandomId()))
                    .withName(FakerData.getRandomName())
                    .withCategory(AddNewPetToStoreRequest.Category.builder()
                            .id(Integer.parseInt(FakerData.getRandomId()))
                            .name("Animal")
                            .build())
                    .please();

            petModelResponse = petClient.addPet(addNewPetToStore)
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(PetModelResponse.class);

            assertAll(
                    () -> assertThat(petModelResponse.getCategory().getName()).isEqualTo("Animal")
            );
            name = petModelResponse.getName();
            id = petModelResponse.getId();
            System.out.println("Pet name is: " + name + ", id is: " + id);
        });

        Allure.step("Step 2: Update an existing pet", () -> {
            UpdateAnExistingPetRequest updateAnExistingPetRequest = BodyGenerator.updateAnExistingPetRequest()
                    .withId(id)
                    .withName(name)
                    .withCategory(UpdateAnExistingPetRequest.Category.builder()
                            .id(1515)
                            .name("Example")
                            .build())
                    .please();

            petModelResponse = petClient.updatePet(updateAnExistingPetRequest)
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(PetModelResponse.class);

            assertAll(
                    () -> assertThat(petModelResponse.getCategory().getName()).isEqualTo("Example")
            );
        });

        Allure.step("Step 3: Search created pet", () -> {
            petModelResponse = petClient.getPet(id)
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(PetModelResponse.class);

            assertAll(
                    () -> assertThat(petModelResponse.getCategory().getName()).isEqualTo("Example")
            );
        });
    }
}
