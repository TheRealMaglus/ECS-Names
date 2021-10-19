package sol;


import java.util.logging.Level;
import org.openqa.selenium.WebDriver;

public class Main {

  /**
   * Starting method of the program, here logging is set to a minimum for selenium as well as
   * methods are called for Microsoft Authentication and ECS Site options menu
   *
   * @param args Arguments given to program, program does not process this
   * @throws InterruptedException Exception caused if program is interrupted while program is
   *                              sleeping
   */
  public static void main(String[] args) throws InterruptedException {

    //Sets logging to a minimum
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    System.setProperty("webdriver.edge.silentOutput", "true");

    //Sets location of msedgedriver for solenium, this is set to same directory as the program
    System.setProperty("webdriver.edge.driver", "msedgedriver.exe");

    //Calls upon MicrosoftLogin class and starts authentication into Microsoft Services
    MicrosoftLogin micro = new MicrosoftLogin();
    WebDriver driver = micro.defaultAuth();

    //Starts main menu of program located inside method go of Site class
    Site site = new Site(driver);
    site.go();
  }
}
