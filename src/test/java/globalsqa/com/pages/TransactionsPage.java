package globalsqa.com.pages;

import globalsqa.com.data.Transaction;
import globalsqa.com.helpers.AllureHelper;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static io.qameta.allure.Allure.step;
import static java.lang.String.format;

public class TransactionsPage {

    private final WebDriver driver;

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By tableTr = By.cssSelector("tr[id^=anchor]");
    private final By tableTd = By.cssSelector("td.ng-binding");

    private List<Transaction> getActualTransactionsData() {
        return driver.findElements(tableTr)
                .stream()
                .map(tr -> {
                    List<WebElement> tdList = tr.findElements(tableTd);
                    return new Transaction(tdList.get(0).getText(),
                            tdList.get(1).getText(), tdList.get(2).getText());
                })
                .collect(Collectors.toList());
    }

    public void checkTransactionsAndAttachCsv(List<Transaction> expectedTransactions) {
        step("Проверить транзакции и приложить отчет", () -> {
            List<Transaction> actualTransactions = getActualTransactionsData();
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
            AllureHelper.attachAsCsvFile("Список транзакций", actualTransactionsDataInCsvFormat);
        });
    }
}
