package sol;

import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Site {

  //Declares all final fields that will be reused throughout the class
  private final WebDriver driver;
  private final Scanner scan = new Scanner(System.in);
  private final By page_info = new ByClassName("current");

  /**
   * Public constructor for this class
   * <p>
   * requires a pre-logged in Microsoft Services WebDriver
   *
   * @param driver WebDriver with authenticated Microsoft Services
   */
  public Site(WebDriver driver) {
    this.driver = driver;
  }

  /**
   * Main outside method to be called within this class
   * <p>
   * Responsible for showing a user repeating main menu of program from which they can select
   * options for program to do
   *
   * @throws InterruptedException called when program is interrupted during sleep
   */
  public void go() throws InterruptedException {
    while (true) {

      System.out.println(
          "Please select the option you would like to proceed in:\n1. Look up a person by username\n2. Quit program");
      int opt;

      //Gets user response as an integer and validates it is a possible option, if not repeats itself
      while (true) {
        opt = this.getIntegerResponse();

        if (opt < 1 || opt > 2) {
          System.out.println("That is not a valid option, please try again");
          continue;
        }
        break;
      }

      //Depending on user input fires up method(s) to deal with user choice
      switch (opt) {
        case 1:
          this.lookUp();
          break;
        case 2:
          System.out.println("Bye!");
          this.driver.quit();

          //Quits program with successful status
          System.exit(0);
          break;
      }
    }
  }

  /**
   * Method that is the whole point of this program
   * <p>
   * Converts SOTON username to a name and outputs it to the user
   *
   * @throws InterruptedException called when program is interrupted during sleep
   */

  private void lookUp() throws InterruptedException {
    System.out.println("Please give me a username of a person:");

    //Gets users response
    String user = scan.nextLine();

    //Checks if username is non-empty, if it is returns to main menu
    if (user.replace(" ", "").length() == 0) {
      System.out.println("You did not type in a username, returning to main menu");
      return;
    }

    //Opens up url with the potential details of the username
    String url = "https://secure.ecs.soton.ac.uk/people/";
    driver.get(url + user);

    //While and when page loads, attempts to find outcome of search
    //Should find either 'Not Found' return or usernames name
    while (true) {
      List<WebElement> page = driver.findElements(page_info);
      if (page.size() != 0) {
        if (page.get(0).getText().equals("Not Found")) {
          System.out.println("Person not found! Press Enter to continue");
          scan.nextLine();
          break;
        }
        System.out.println(
            "Persons name is: " + page.get(0).getText() + ". Press Enter to continue");
        scan.nextLine();
        break;
      }
      //Sleep of 1 sec between attempts at search
      Thread.sleep(1000);

    }


  }


  /**
   * Method to request input of user, and try to convert it to an Integer If conversion fails, asks
   * user to enter a valid number(Integer)
   *
   * @return Integer that user inputted
   */
  private int getIntegerResponse() {
    int num;
    while (true) {
      String opt = scan.nextLine();
      try {
        num = Integer.parseInt(opt);
        return num;
      } catch (NumberFormatException e) {
        System.out.println("That is not a valid number, please try again");
      }
    }

  }
}
