import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TodoTest {
    WebDriver driver;
    WebDriverWait webDriverWait;
    Actions actions ;
    @BeforeAll
    public static void initialize() {
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    public void prepareDriver(){
        driver = new ChromeDriver();
        webDriverWait=new WebDriverWait(driver,10);
        Actions actions = new Actions(driver);

    }
    @ParameterizedTest
    @ValueSource(strings = {
            "Backbone.js",
            "AngularJS",
            "Dojo",
            "React"
    })
    public void testTodoWithTechno(String techno){
        driver.navigate().to("https://todomvc.com/");
        openTechnology(techno);
        addNewTodoItem("Car wash 1");
        addNewTodoItem("Car wash 2");
        addNewTodoItem("Car wash 3");
        removeTodo("Car wash 3");
        assertNumberOfLeftItems(2);
    }
    @Test
    public void testTodo(){
        driver.navigate().to("https://todomvc.com/");
        openTechnology("Backbone.js");
        addNewTodoItem("Car wash 1");
        addNewTodoItem("Car wash 2");
        addNewTodoItem("Car wash 3");
        removeTodo("Car wash 3");
        assertNumberOfLeftItems(2);
    }
    public void addNewTodoItem(String todoItem){
        WebElement todoInput=waitAndFindElement(By.xpath("//input[@placeholder='What needs to be done?']\n"));
        todoInput.sendKeys(todoItem);
        Actions actions = new Actions(driver);
        actions.click(todoInput).sendKeys(Keys.ENTER).perform();
    }

    private void removeTodo(String todoName) {
        WebElement element = driver.findElement(By.xpath(String.format("//label[text()='%s']/preceding-sibling::input",todoName)));
        element.click();
    }
    public void openTechnology(String technologyName){
        WebElement technologyLink= waitAndFindElement(By.linkText(technologyName));
        technologyLink.click();
    }
    public WebElement waitAndFindElement(By locator){
        return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    private void assertNumberOfLeftItems(int expectedLeft) {
        WebElement element = driver.findElement(By.xpath("//footer/*/span | //footer/span"));
        String expectedTest = String.format("%d %s left", expectedLeft, expectedLeft == 1 ? "item": "items");
        validateInnerText(element,expectedTest);
    }
    private void validateInnerText(WebElement element, String expectedTest) {
        ExpectedConditions.textToBePresentInElement(element, expectedTest);
    }
    @AfterEach
    public void quitDriver() throws InterruptedException {
        driver.quit();
    }

}
