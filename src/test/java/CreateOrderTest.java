import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.praktikum.model.DeliverPageAboutRent;
import ru.yandex.praktikum.model.DeliverPageWhoIsScooterFor;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    //private ChromeDriver driver;
    //на хроме тест падает, поэтому использовался Firefox
    private FirefoxDriver driver;
    private final static String URL = "https://qa-scooter.praktikum-services.ru/";

    //Модальное окно Заказ оформлен
    private final static By ORDER_PLACED_WINDOW = By.className("Order_ModalHeader__3FDaJ");
    private String firstName;
    private String lastName;
    private String deliverAddress;
    private String phoneNumber;

    public CreateOrderTest(String firstName, String lastName, String deliverAddress, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.deliverAddress = deliverAddress;
        this.phoneNumber = phoneNumber;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][] {
                {"Василий", "Иванов", "Москва, Ленина, 23", "+79976195794"},
                {"Иван", "Васильев", "Москва, Сталина, 57", "+79961235769"},
        };
    }

    @Before
    public void setUp() {
        //WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.gecko.driver", "C:\\WebDriver\\bin\\firefoxDriver\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.get(URL);
        new WebDriverWait(driver, 3);
    }

    @Test
    public void createOrdersWithDifferentDataTest() {
        DeliverPageWhoIsScooterFor orderFirstPage = new DeliverPageWhoIsScooterFor(driver);
        orderFirstPage.headerOrderButtonClick();
        orderFirstPage.firstNameSendKeys(firstName);
        orderFirstPage.lastNameSendKeys(lastName);
        orderFirstPage.deliverAddressSendKeys(deliverAddress);
        orderFirstPage.chooseMetroStation();
        orderFirstPage.phoneNumberSendKeys(phoneNumber);
        orderFirstPage.nextButtonClick();

        new WebDriverWait(driver, 2);

        DeliverPageAboutRent orderSecondPage = new DeliverPageAboutRent(driver);
        orderSecondPage.chooseDeliverDate();
        new WebDriverWait(driver, 2);
        orderSecondPage.chooseDeliverDate();
        orderSecondPage.chooseDurationOfDays();
        orderSecondPage.middleOrderButtonCLick();
        orderSecondPage.confirmOrderClick();

        //Проверяется, что заголовок на окне с завершением заказа отображается после совершения заказа
        Assert.assertTrue("Expected display modal window Order created", driver.findElement(ORDER_PLACED_WINDOW).isDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }

}
