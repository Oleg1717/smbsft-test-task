package globalsqa.com.pages;

import globalsqa.com.data.Customer;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class CustomerAccountPage {

    private final WebDriver driver;

    public CustomerAccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[@class='fontBig ng-binding']")
    private WebElement customerName;
    @FindBy(xpath = "//strong[@class='ng-binding']")
    private List<WebElement> accountDataItems;

    @FindBy(xpath = "//button[@ng-click='transactions()']")
    private WebElement transactionsButton;
    @FindBy(xpath = "//button[@ng-click='deposit()']")
    private WebElement depositButton;
    @FindBy(xpath = "//button[@ng-click='withdrawl()']")
    private WebElement withdrawlButton;

    @FindBy(xpath = "//span[@ng-show='message']")
    private WebElement amountActionMessage;
    @FindBy(xpath = "//div[@class='form-group']//label")
    private WebElement amountActionLabel;
    @FindBy(xpath = "//input[@ng-model='amount']")
    private WebElement amountValueInput;
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement amountActionButton;

    @Step("Выполнить пополнение и списание, проверить баланс")
    public TransactionsPage makeDepositWithdrawAndCheckBalance(@Param(mode = HIDDEN) Customer customer,
                                                               @Param(mode = HIDDEN) String amountValue,
                                                               @Param(mode = HIDDEN) String expectedBalance) {
        checkCustomerName(customer.name_());
        makeDeposite(amountValue);
        makeWithdrawl(amountValue);
        checkBalanceValue(expectedBalance);
        return clickTransactionsButton();
    }

    @Step("Выполнить пополнение счета")
    public CustomerAccountPage makeDeposite(@Param(mode = HIDDEN) String amountValue) {
        clickDepositButton();
        checkActionLabel("Amount to be Deposited :");
        inputAmountValue(amountValue);
        clickActionButton("Deposit");
        checkActionMessage("Deposit Successful");
        return this;
    }

    @Step("Выполнить списание со счета")
    public CustomerAccountPage makeWithdrawl(@Param(mode = HIDDEN) String amountValue) {
        clickWithdrawlButton();
        checkActionLabel("Amount to be Withdrawn :");
        inputAmountValue(amountValue);
        clickActionButton("Withdraw");
        checkActionMessage("Transaction successful");
        return this;
    }

    @Step("Имя пользователя на странице аккаунта = {name}")
    public CustomerAccountPage checkCustomerName(@Param(mode = HIDDEN) String name) {
        Assertions.assertThat(customerName.getText()).isEqualTo(name);
        return this;
    }

    @Step("Баланс счета = {expectedBalance}")
    public CustomerAccountPage checkBalanceValue(@Param(mode = HIDDEN) String expectedBalance) {
        String actualBalance = accountDataItems.get(1).getText();
        Assertions.assertThat(actualBalance).isEqualTo(expectedBalance);
        return this;
    }

    @Step("Кликнуть кнопку 'Transactions'")
    public TransactionsPage clickTransactionsButton() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        transactionsButton.click();
        return new TransactionsPage(driver);
    }

    @Step("Кликнуть кнопку 'Deposit'")
    public CustomerAccountPage clickDepositButton() {
        depositButton.click();
        return this;
    }

    @Step("Кликнуть кнопку 'Withdrawl'")
    public CustomerAccountPage clickWithdrawlButton() {
        withdrawlButton.click();
        return this;
    }

    @Step("Наименование поля 'amount' = {expectedLabelText}")
    public CustomerAccountPage checkActionLabel(@Param(mode = HIDDEN) String expectedLabelText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        wait.until(ExpectedConditions.textToBePresentInElement(amountActionLabel, expectedLabelText));
        return this;
    }

    @Step("Ввести в поле 'amount' значение = {amountValue}")
    public CustomerAccountPage inputAmountValue(@Param(mode = HIDDEN) String amountValue) {
        amountValueInput.sendKeys(amountValue);
        return this;
    }

    @Step("Кликнуть кнопку '{buttonText}'")
    public CustomerAccountPage clickActionButton(@Param(mode = HIDDEN) String buttonText) {
        amountActionButton.click();
        return this;
    }

    @Step("Сообщение поля 'amount' = {expectedActionMessage}")
    public CustomerAccountPage checkActionMessage(@Param(mode = HIDDEN) String expectedActionMessage) {
        Assertions.assertThat(amountActionMessage.getText()).isEqualTo(expectedActionMessage);
        return this;
    }
}
