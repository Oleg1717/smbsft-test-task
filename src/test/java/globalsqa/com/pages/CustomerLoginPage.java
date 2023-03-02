package globalsqa.com.pages;

import globalsqa.com.data.Customer;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class CustomerLoginPage {

    private final WebDriver driver;

    public CustomerLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "select#userSelect")
    private WebElement customerSelect;
    @FindBy(css = "option.ng-binding.ng-scope")
    private List<WebElement> customersList;
    @FindBy(css = "button[type=submit]")
    private WebElement loginButton;

    @Step("Авторизоваться как '{customer.name}'")
    public CustomerAccountPage authoriseAsCustomer(@Param(mode = HIDDEN) Customer customer) {
        selectCustomer(customer);
        return clickLoginButton();
    }

    @Step("Выбрать пользователя '{customer.name}'")
    public void selectCustomer(@Param(mode = HIDDEN) Customer customer) {
        customerSelect.click();
        customersList.get(customer.ordinal()).click();
    }

    @Step("Кликнуть кнопку 'Login'")
    public CustomerAccountPage clickLoginButton() {
        loginButton.click();
        return new CustomerAccountPage(driver);
    }
}
