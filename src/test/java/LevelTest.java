/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

import java.util.Scanner;

/**
 * Created by Ethan on 1/29/2017.
 */
public class LevelTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long requiredKills = 5;
        int level = 1;
        int kills = 0;
        while(scanner.hasNext()) {
            if(scanner.nextLine().equalsIgnoreCase("kill")) {
                kills++;
                System.out.println("Increased kills: " + kills);
                if(kills >= requiredKills) {
                    requiredKills = Math.round(requiredKills*1.75);
                    level++;
                    System.out.println("--------------------------------------");
                    System.out.println("You have leveled up! [Level " + level + "]");
                    System.out.println("--------------------------------------");

                }
            }
        }
    }

}
