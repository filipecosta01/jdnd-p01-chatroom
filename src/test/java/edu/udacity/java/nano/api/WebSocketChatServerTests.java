package edu.udacity.java.nano.api;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketChatServerTests extends BaseSeleniumTests {
    @Test
    public void verifyOpenLoginPage() {
        this.driver.get("http://localhost:8080");
        WebElement inputElement = this.driver.findElement(By.id("username"));
        assertNotNull(inputElement);
    }

    @Test
    public void verifyDoLogin() {
        this.driver.get("http://localhost:8080");
        final WebElement inputElement = this.driver.findElement(By.id("username"));
        final WebElement submitElement = this.driver.findElement(By.className("submit"));
        inputElement.sendKeys("foo");
        submitElement.click();
        assertNotNull(inputElement);
    }

    @Test
    public void verifyCurrentUser() throws InterruptedException {
        this.driver.get("http://localhost:8080");
        WebElement inputElement = this.driver.findElement(By.id("username"));
        WebElement submitElement = this.driver.findElement(By.className("submit"));
        inputElement.sendKeys("test");
        submitElement.click();
        assertNotNull(inputElement);

        // Wait chat page to load
        Thread.sleep(100);

        final WebElement currentUser = this.driver.findElement(By.id("username"));
        assertNotNull(currentUser);
        assertEquals(currentUser.getText(), "test");
    }

    @Test
    public void verifyOnlineUsers() throws InterruptedException {
        this.driver.get("http://localhost:8080");
        final WebElement inputElement = this.driver.findElement(By.id("username"));
        final WebElement submitElement = this.driver.findElement(By.className("submit"));
        inputElement.sendKeys("foo");
        submitElement.click();
        assertNotNull(inputElement);

        // Wait chat page to load
        Thread.sleep(100);

        final WebElement onlineUsers = this.driver.findElement(By.className("chat-num"));
        assertNotNull(onlineUsers);
        assertEquals(onlineUsers.getText(), "1");
    }

    @Test
    public void verifyMultipleOnlineUsers() throws InterruptedException {
        this.driver.get("http://localhost:8080");
        final WebElement inputElement = this.driver.findElement(By.id("username"));
        final WebElement submitElement = this.driver.findElement(By.className("submit"));
        inputElement.sendKeys("foo");
        submitElement.click();
        assertNotNull(inputElement);

        // Open a new window and connect to server
        ((JavascriptExecutor) this.driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        this.driver.switchTo().window(tabs.get(1));

        this.driver.get("http://localhost:8080");
        final WebElement inputElementNewTab = this.driver.findElement(By.id("username"));
        final WebElement submitElementNewTab = this.driver.findElement(By.className("submit"));
        inputElementNewTab.sendKeys("bar");
        submitElementNewTab.click();
        assertNotNull(inputElementNewTab);

        // Wait chat page to load
        Thread.sleep(100);

        final WebElement onlineUsersNewTab = this.driver.findElement(By.className("chat-num"));
        assertNotNull(onlineUsersNewTab);
        assertEquals(onlineUsersNewTab.getText(), "2");

        // Switch back to original tab and check number of users
        Thread.sleep(100);
        this.driver.switchTo().window(tabs.get(0));
        final WebElement onlineUsers = this.driver.findElement(By.className("chat-num"));
        assertNotNull(onlineUsers);
        assertEquals(onlineUsers.getText(), "2");

        // Wait for the online message to be sent and tab switches
        Thread.sleep(100);
        this.driver.switchTo().window(tabs.get(1));

        this.driver.close();
        // Wait for the message to be received and page updated
        Thread.sleep(100);
    }

    @Test
    public void verifySendMessages() throws InterruptedException {
        this.driver.get("http://localhost:8080");
        final WebElement inputElement = this.driver.findElement(By.id("username"));
        final WebElement submitElement = this.driver.findElement(By.className("submit"));
        inputElement.sendKeys("foo");
        submitElement.click();
        assertNotNull(inputElement);

        // Wait chat page to load
        Thread.sleep(100);

        final WebElement msgInputElement = this.driver.findElement(By.id("msg"));
        final WebElement submitMessageElement = this.driver.findElement(By.name("submit-msg"));
        msgInputElement.sendKeys("test message");
        submitMessageElement.click();

        assertNotNull(msgInputElement);

        // Wait for the message to be sent and tab switches
        Thread.sleep(100);
        final WebElement messageContainerElement = this.driver.findElement(By.className("message-container"));
        assertNotNull(messageContainerElement);
        final WebElement firstMessageElement = messageContainerElement.findElement(By.className("message-content"));
        assertTrue(firstMessageElement.getText().contains("test message"));
    }

    @Test
    public void verifyDisconnectedUser() throws InterruptedException {
        this.driver.get("http://localhost:8080");
        final WebElement inputElement = this.driver.findElement(By.id("username"));
        final WebElement submitElement = this.driver.findElement(By.className("submit"));
        inputElement.sendKeys("foo");
        submitElement.click();
        assertNotNull(inputElement);

        // Open a new window and connect to server
        ((JavascriptExecutor) this.driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        this.driver.switchTo().window(tabs.get(1));

        this.driver.get("http://localhost:8080");
        final WebElement inputElementNewTab = this.driver.findElement(By.id("username"));
        final WebElement submitElementNewTab = this.driver.findElement(By.className("submit"));
        inputElementNewTab.sendKeys("bar");
        submitElementNewTab.click();
        assertNotNull(inputElementNewTab);

        // Wait chat page to load
        Thread.sleep(100);

        final WebElement onlineUsersNewTab = this.driver.findElement(By.className("chat-num"));
        assertNotNull(onlineUsersNewTab);
        assertEquals(onlineUsersNewTab.getText(), "2");

        // Switch back to original tab and check number of users
        Thread.sleep(100);
        this.driver.switchTo().window(tabs.get(0));
        final WebElement onlineUsers = this.driver.findElement(By.className("chat-num"));
        assertNotNull(onlineUsers);
        assertEquals(onlineUsers.getText(), "2");
        this.driver.close();

        // Wait for the online message to be sent and tab switches
        Thread.sleep(200);
        this.driver.switchTo().window(tabs.get(1));
        Thread.sleep(100);

        final WebElement onlineUsersAfterDisconnection = this.driver.findElement(By.className("chat-num"));
        assertNotNull(onlineUsersAfterDisconnection);
        assertEquals(onlineUsersAfterDisconnection.getText(), "1");

        this.driver.close();
        // Wait for the message to be received and page updated
        Thread.sleep(100);
    }
}
