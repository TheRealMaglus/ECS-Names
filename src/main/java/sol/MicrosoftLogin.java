package sol;

import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MicrosoftLogin {

  //Declares WebDriver, we will be using default EdgeBrowser and hence Edge Driver - so only works for newish Win 10 and Win 11 by default
  private final WebDriver driver;
  //Defines and initialises all the HTML fields that will be required to authenticate into Microsoft Services
  By email = By.cssSelector("input[type=email]");
  By submit = By.cssSelector("input[type=submit]");
  By passwd = By.cssSelector("input[type=password]");
  By auth_id = By.id("idDiv_SAOTCAS_Title");
  By back_button = By.id("idBtn_Back");
  By passwd_error = By.id("passwordError");
  By username_error = By.id("usernameError");
  //Defines login credentials fields as well as Driver, used to emulate the browser
  //and options with which the driver is run
  private String login;
  private String password;
  private EdgeOptions options = new EdgeOptions();


  /**
   * Constructor for MicrosoftLogin class takes in no parameters inputs and initialises the driver
   * with invisible option
   */
  public MicrosoftLogin() {
    this.setInvisible();
    this.driver = new EdgeDriver(options);
  }


  /**
   * Main method for MicrosoftLogin class, that will be called from outside the class
   * <p>
   * This method focuses on harvesting and checking users credentials into Microsoft Services and
   * attempting to resolve problems related to incorrect credentials or wrong format of credentials
   * <p>
   * To verify credentials authenticate method is run
   *
   * @return WebDriver located inside this class
   * @throws InterruptedException if program is interrupted during sleep
   */
  public WebDriver defaultAuth() throws InterruptedException {
    {
      //Object to read direct input from console
      Scanner scan = new Scanner(System.in);

      //defines variables for credentials
      String username;
      String password;

      //Outer while(true) loop that will continue until user has successfully been logged in
      while (true) {

        //Loop to get and check non-empty username to authenticate
        while (true) {
          System.out.println("Please enter your soton username:");
          username = scan.nextLine();
          if (username.replaceAll(" ", "").length() == 0) {
            System.out.println("You can not leave username blank, please try again");
            continue;
          }
          break;
        }

        //Loop to get and check non-empty password to authenticate
        while (true) {
          password = String.valueOf(
              System.console().readPassword("Please enter your soton password:"));
          if (password.replaceAll(" ", "").length() == 0) {
            System.out.println("You can not leave password blank, please try again");
            continue;
          }
          break;
        }

        //Sets value of global login and password variables, and adds domain onto username
        this.login = username + "@soton.ac.uk";
        this.password = password;

        //Attempts authentication and if successful, returns the WebDriver
        if (this.authenticate()) {
          return this.driver;
        }

        //This is only possible if authentication failed, notifies user of so
        // and repeats outer while loop when input is received(Enter for user but any input works)
        System.out.println("Credentials are incorrect, press enter to try again");
        scan.nextLine();

      }


    }
  }


  /**
   * Main internal method to open up Microsoft Login page using selenium and Authenticate using
   * global login and password credentials defined in this class
   *
   * @return outcome of authentication, true for successful, false otherwise
   * @throws InterruptedException when interrupted during sleep
   */

  private boolean authenticate() throws InterruptedException {

    //Creates object that allows Selenium to wait until page elements are visible or loaded in, with max wait of 10 sec
    WebDriverWait wait = new WebDriverWait(driver, 10);

    //opens url that method will try to authenticate into, with redirections ON(by default)
    String url = "https://secure.ecs.soton.ac.uk/";
    driver.get(url);

    //waits until email field is visible, then inputs into it and click button Next
    wait.until(ExpectedConditions.visibilityOfElementLocated(email)).sendKeys(this.login);
    wait.until(ExpectedConditions.visibilityOfElementLocated(submit)).click();

    //Double check loop, checks every 1sec if username is invalid or password field is shown(hence assumed username valid)
    // If invalid username, returns false and ends method
    while (true) {
      Thread.sleep(1000);
      //List of elements that may contain error field(should only be size 1 if found)
      List<WebElement> error = driver.findElements(this.username_error);

      //List of elements that may contain password field(should only be size 1 if found)
      List<WebElement> passwd = driver.findElements(this.passwd);

      //if error field is found, return false for method
      if (error.size() != 0 && error.get(0).isDisplayed()) {
        return false;
      }
      //if password field is found, break out the loop
      if (passwd.size() != 0 && passwd.get(0).isDisplayed()) {
        break;
      }

    }

    //Makes sure both password field and submit button are fully visible, then submits password and clicks the button
    wait.until(ExpectedConditions.visibilityOfElementLocated(passwd)).sendKeys(this.password);
    wait.until(ExpectedConditions.elementToBeClickable(submit)).click();

    //Sleep of 3 sec to allow page to load
    Thread.sleep(3000);

    //Checks if credential authentication was successful
    List<WebElement> login_errors = driver.findElements(passwd_error);
    if (login_errors.size() != 0) {
      return false;
    }

    //Checks if 2FA is required, and if so asks user to click approve on their phone
    //Only works with Microsoft Authentication(connected to internet) in current implementation
    boolean said = false;
    while (true) {
      List<WebElement> auth = driver.findElements(auth_id);
      if (auth.size() == 0) {
        break;
      }
      if (!said) {
        System.out.println("2FA Authentication is required, please click approve on your device");
        said = true;
      }
      //We all need some sleep, and so does program to not overload the PC and webdriver with requests
      Thread.sleep(1000);
    }

    //Checks if final submit button exists(button to login without saving credentials), if so clicks it and logs in!
    wait.until(ExpectedConditions.elementToBeClickable(back_button)).click();

    //Informs user of success
    System.out.println("Logged in!");

    //Informs parent method of success
    return true;
  }

  /**
   * Sets options of driver to run invisibly
   */
  public void setInvisible() {
    this.options.addArguments("--headless");
  }


  /**
   * Resets the options of the driver
   */
  public void setVisible() {
    this.options = new EdgeOptions();
  }


}
