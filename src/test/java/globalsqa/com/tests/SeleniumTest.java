package globalsqa.com.tests;

import globalsqa.com.config.ProjectConfig;
import globalsqa.com.data.Customer;
import globalsqa.com.data.Transaction;
import globalsqa.com.data.TransactionType;
import globalsqa.com.pages.CustomerAccountPage;
import globalsqa.com.pages.CustomerLoginPage;
import globalsqa.com.pages.MainPage;
import globalsqa.com.pages.TransactionsPage;
import globalsqa.com.utils.MathUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SeleniumTest {

    private WebDriver driver;
    private final ProjectConfig projectConfig = ConfigFactory.newInstance()
            .create(ProjectConfig.class, System.getProperties());

    private CustomerLoginPage customerLoginPage;
    private CustomerAccountPage customerAccountPage;
    private TransactionsPage transactionsPage;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(projectConfig.browserName());
        driver = new RemoteWebDriver(new URL(projectConfig.seleniumGridUrl()), capabilities);
        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(30))
                .implicitlyWait(Duration.ofSeconds(4));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    @Description("Необходимо открыть тестовую страницу, авторизоваться тестовым пользователем, " +
            "выполнить пополнение и списание баланса суммой " +
            "вычисленной на основе текущего числа месяца с использованием чисел фибоначчи, " +
            "выполнить проверку баланса, проверить список транзакций " +
            "и сформировать на его основе файл экспорта в формате csv")
    @DisplayName("Тест банковского аккаунта пользователя Harry Potter")
    void bankAccountTest() {
        String testUrl = projectConfig.baseUrl() + "/angularJs-protractor/BankingProject/#/login";
        Customer testCustomer = Customer.HARRY_POTTER;
        String expectedBalance = "0";
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        String amountValue = String.valueOf(MathUtils.calcFibonacciNumber(dayOfMonth + 1));
        List<Transaction> expectedTransactionsData = List.of(
                new Transaction(amountValue, TransactionType.CREDIT.value()),
                new Transaction(amountValue, TransactionType.DEBIT.value()));

        Allure.parameter("Тестовая страница", testUrl);
        Allure.parameter("Тестовый пользователь", testCustomer.name_());
        Allure.parameter("Сумма пополнения", amountValue);

        customerLoginPage = new MainPage(driver).openPageAndEnterAsCustomer(testUrl);
        customerAccountPage = customerLoginPage.authoriseAsCustomer(testCustomer);
        transactionsPage = customerAccountPage
                .makeDepositWithdrawAndCheckBalance(testCustomer, amountValue, expectedBalance);
        transactionsPage.checkTransactionsAndAttachCsv(expectedTransactionsData);
    }
}
