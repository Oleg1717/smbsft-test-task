package globalsqa.com.pages;

import io.qameta.allure.Param;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class MainPage {

    private final WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "button[ng-click='customer()']")
    private WebElement customerLoginButton;

    @Step("Открыть тестовую страницу и войти как покупатель (customer)")
    public CustomerLoginPage openPageAndEnterAsCustomer(@Param(mode = HIDDEN) String url) {
        openPageByUrl(url);
        clickCustomerLoginButton();
        return new CustomerLoginPage(driver);
    }

    @Step("Открыть тестовую страницу")
    public void openPageByUrl(@Param(mode = HIDDEN) String url) {
        driver.get(url);
    }

    @Step("Кликнуть кнопку 'Customer Login'")
    public void clickCustomerLoginButton() {
        customerLoginButton.click();
    }
}
