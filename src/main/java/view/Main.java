package view;

import controller.Controller;
import controller.ControllerForView;

public class Main {


    public static void main(String[] args) {
        Controller controller = new Controller();
        System.out.println("Starting ***");
        System.out.println(controller.login("joku", "joku"));

    }
}
