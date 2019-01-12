package core;
import java.io.File;
import java.util.Scanner;
import core.controller.SimulatorController;

public class CommandLineRunner {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        final File scenarioFile = new File("./src/main/resources/test_scenario.txt");
        final File statisticsFile = new File("./src/main/resources/statistics 0-10 for test_scenario.txt");
        SimulatorController sim = new SimulatorController( scenarioFile, statisticsFile);
        String nextLine;
        while (!(nextLine = s.nextLine()).equals("quit")) {
            String[] commands = nextLine.split(" ");
            switch (commands[0]) {
                case "n":
                case "next":
                    sim.nextEvent();
                    break;
                case "r":
                case "reset":
                    sim = new SimulatorController(  scenarioFile, statisticsFile);
                    break;
                case "chs":
                    if (commands.length == 3) {
                        sim.changeSpeed(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
                        break;
                    }
                case "chc":
                    if (commands.length == 3) {
                        sim.changeCapacity(Integer.parseInt(commands[1]), Integer.parseInt(commands[2]));
                        break;
                    }
                default:
                    System.out.println("Error: Invalid Command");
            }
        }
    }

}
