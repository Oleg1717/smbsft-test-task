package globalsqa.com.pages;

import globalsqa.com.config.ProjectConfig;
import globalsqa.com.data.Transaction;
import globalsqa.com.helpers.AllureHelper;
import globalsqa.com.utils.FileUtils;
import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class TransactionsPage {

    private final WebDriver driver;
    private final ProjectConfig projectConfig = ConfigFactory.newInstance()
            .create(ProjectConfig.class, System.getProperties());

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "tr[id^=anchor]")
    private List<WebElement> tableTr;

    private List<Transaction> getActualTransactionsData() {
        driver.navigate().refresh();
        return tableTr
                .stream()
                .map(tr -> {
                    List<WebElement> tdList = tr.findElements(By.cssSelector("td.ng-binding"));
                    return new Transaction(tdList.get(0).getText(),
                            tdList.get(1).getText(), tdList.get(2).getText());
                })
                .collect(Collectors.toList());
    }

    public void checkTransactionsAndAttachCsv(List<Transaction> expectedTransactions) {
        step("Проверить транзакции и приложить отчет", () -> {
            List<Transaction> actualTransactions = Awaitility
                    .with().pollInterval(2, SECONDS)
                    .atMost(10, SECONDS)
                    .given()
                    .await().until(this::getActualTransactionsData, not(empty()));
            checkTransactionsCount(actualTransactions.size(), expectedTransactions.size());
            checkTransactions(actualTransactions, expectedTransactions);
            exportActualTransactionsToCsv(actualTransactions);
        });
    }

    public void checkTransactionsCount(int actualTransactionsSize, int expectedTransactionsSize) {
        step("Количество транзакций в таблице = " + expectedTransactionsSize,
                () -> Assertions.assertThat(actualTransactionsSize)
                        .overridingErrorMessage(format("Ожидаемое количество транзакций в списке - %s, фактическое - %s",
                                actualTransactionsSize, expectedTransactionsSize))
                        .isEqualTo(expectedTransactionsSize));
    }

    public void checkTransactions(List<Transaction> actualTransactions, List<Transaction> expectedTransactions) {
        step("Проверить транзакции", () -> {
            Assertions.assertThat(actualTransactions)
                    .usingRecursiveComparison()
                    .ignoringFields("dateTime")
                    .isEqualTo(expectedTransactions);
        });
    }

    public void exportActualTransactionsToCsv(List<Transaction> actualTransaction) {
        step("Приложить отчет о транзакциях в формате csv", () -> {
            DateTimeFormatter expectedFormat = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss");
            String actualTransactionsDataInCsvFormat = actualTransaction.stream()
                    .map(t -> String.join(";",
                            t.dateTime().format(expectedFormat), t.amount(), t.transactionType()))
                    .collect(Collectors.joining("\n"));
            FileUtils.writeDataToFile(projectConfig.csvFilePath(), actualTransactionsDataInCsvFormat);
            AllureHelper.attachCsvFile("Список транзакций", projectConfig.csvFilePath());
        });
    }
}
