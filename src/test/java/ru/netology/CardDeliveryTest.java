package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryTest {

    public String deliveryDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    public String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldGetSuccessMessage() {
        $("[data-test-id='city'] input").setValue("Тверь");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иван Иванов");
        $("[data-test-id='phone'] input").setValue("+89012345678");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(appear, Duration.ofSeconds(15));
        $("[data-test-id='notification']").shouldHave(text("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldGetErrorDueEmptyCheckbox() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Семен Петров");
        $("[data-test-id='phone'] input").setValue("+89010098765");
        $(byText("Забронировать")).click();
        $(byClassName("checkbox__text")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorDueInvalidName() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Invalid Name");
        $("[data-test-id='phone'] input").setValue("+81234567865");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byClassName("input__sub")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorDueInvalidCharactersInName() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Полина См1рнова");
        $("[data-test-id='phone'] input").setValue("+81234567865");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byClassName("input__sub")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorDueInvalidDate() {
        $("[data-test-id='city'] input").setValue("Нижний Новгород");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(today);
        $("[data-test-id='name'] input").setValue("Антонова Елена");
        $("[data-test-id='phone'] input").setValue("+81234617864");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byText("Заказ на выбранную дату невозможен")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorDueInvalidPhone() {
        $("[data-test-id='city'] input").setValue("Уфа");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Петрова Анна");
        $("[data-test-id='phone'] input").setValue("81234617864");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorDueUnavailableCity() {
        $("[data-test-id='city'] input").setValue("Петергоф");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Андреева Анна");
        $("[data-test-id='phone'] input").setValue("+81234617864");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byText("Доставка в выбранный город недоступна")).shouldBe(visible);
    }
}
