package br.ufma.lsdi.authentication;

import br.ufma.lsdi.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScopusLogin implements LoginDigitalLibrary {

    private boolean statusLogin;

    @Override
    public boolean authentication(WebDriver webDriver, String email, String password) {
        webDriver.get("https://www.scopus.com/home.uri");

        WebElement buttonSignIn = webDriver.findElement(By.xpath("//*[@id=\"signin_link_move\"]"));
        buttonSignIn.click();

        Utils.sleep(3000);

        WebElement inputEmail = webDriver.findElement(By.xpath("//*[@id=\"bdd-email\"]"));
        inputEmail.sendKeys(email);

        WebElement buttonContinue = webDriver.findElement(By.xpath("//*[@id=\"bdd-elsPrimaryBtn\"]"));
        buttonContinue.click();

        Utils.sleep(3000);

        WebElement inputPassword = webDriver.findElement(By.xpath("//*[@id=\"bdd-password\"]"));
        inputPassword.sendKeys(password);

        WebElement buttonSignInFinal = webDriver.findElement(By.xpath("//*[@id=\"bdd-elsPrimaryBtn\"]"));
        buttonSignInFinal.click();

        Utils.sleep(3000);

        this.statusLogin = true;
        return true;
    }

    @Override
    public boolean isStatusLogin() {
        return this.statusLogin;
    }

}
