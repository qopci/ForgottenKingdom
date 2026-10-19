
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class MainTest {

    static int passed = 0;
    static int failed = 0;

    interface TestAction {
        void run() throws Exception;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("==================================");
        System.out.println("  FORGOTTEN KINGDOM TEST SUITE");
        System.out.println("==================================\n");

        // Character creation and starting stats
        runTest("Warrior starting stats", () -> {
            Main.Player p = new Main.Player("Alex", "Warrior");
            check(p.hp == 120, "Warrior HP should be 120");
            check(p.maxHP == 120, "Warrior max HP should be 120");
            check(p.attack == 15, "Warrior attack should be 15");
            check(p.mana == 10, "Warrior mana should be 10");
            check(p.gold == 30, "Starting gold should be 30");
            check(p.potions == 2, "Starting potions should be 2");
        });

        runTest("Mage starting stats", () -> {
            Main.Player p = new Main.Player("Merlin", "Mage");
            check(p.hp == 80, "Mage HP should be 80");
            check(p.attack == 10, "Mage attack should be 10");
            check(p.mana == 50, "Mage mana should be 50");
        });

        runTest("Rogue starting stats", () -> {
            Main.Player p = new Main.Player("Shadow", "Rogue");
            check(p.hp == 100, "Rogue HP should be 100");
            check(p.attack == 13, "Rogue attack should be 13");
            check(p.mana == 20, "Rogue mana should be 20");
        });

        runTest("Player starts at level 1 with zero XP", () -> {
            Main.Player p = new Main.Player("Alex", "Warrior");
            check(p.level == 1, "Starting level should be 1");
            check(p.xp == 0, "Starting XP should be 0");
            check(p.xpToNextLevel == 100, "First XP target should be 100");
        });

        runTest("Monster stores its stats", () -> {
            Main.Monster m = new Main.Monster("Goblin", 35, 8);
            check(m.name.equals("Goblin"), "Monster name incorrect");
            check(m.hp == 35, "Monster HP incorrect");
            check(m.attack == 8, "Monster attack incorrect");
        });

        // Combat mechanics
        runTest("Normal attack deals damage in expected range", () -> {
            reset("Warrior");
            Main.Monster m = new Main.Monster("Test Enemy", 500, 0);

            Main.normalAttack(m);

            int damage = 500 - m.hp;
            check(damage >= 15 && damage <= 20,
                    "Warrior normal damage should be 15-20");
        });

        runTest("Warrior Power Strike deals correct damage range", () -> {
            reset("Warrior");
            Main.Monster m = new Main.Monster("Test Enemy", 500, 0);

            Main.useAbility(m);

            int damage = 500 - m.hp;
            check(damage >= 30 && damage <= 35,
                    "Power Strike should deal 30-35 damage");
        });

        runTest("Mage Fireball deals damage and costs mana", () -> {
            reset("Mage");
            Main.Monster m = new Main.Monster("Test Enemy", 500, 0);

            Main.useAbility(m);

            int damage = 500 - m.hp;
            check(damage >= 25 && damage <= 35,
                    "Fireball should deal 25-35 damage");
            check(Main.player.mana == 35,
                    "Fireball should consume 15 mana");
        });

        runTest("Mage with insufficient mana deals no damage", () -> {
            reset("Mage");
            Main.player.mana = 10;
            Main.Monster m = new Main.Monster("Test Enemy", 100, 0);

            Main.useAbility(m);

            check(m.hp == 100, "Enemy HP should not change");
            check(Main.player.mana == 10, "Mana should not change");
        });

        runTest("Rogue Quick Strike deals correct damage range", () -> {
            reset("Rogue");
            Main.Monster m = new Main.Monster("Test Enemy", 500, 0);

            Main.useAbility(m);

            int damage = 500 - m.hp;
            check(damage >= 18 && damage <= 33,
                    "Quick Strike should deal 18-33 damage");
        });

        runTest("Damage cannot reduce enemy HP below zero", () -> {
            reset("Warrior");
            Main.Monster m = new Main.Monster("Weak Enemy", 1, 0);

            Main.normalAttack(m);

            check(m.hp == 0, "Enemy HP should stop at zero");
        });

        // XP and leveling
        runTest("Gaining XP without leveling", () -> {
            reset("Warrior");

            captureOutput(() -> Main.gainXP(50));

            check(Main.player.xp == 50, "XP should be 50");
            check(Main.player.level == 1, "Level should remain 1");
        });

        runTest("Exact XP threshold triggers level up", () -> {
            reset("Warrior");

            captureOutput(() -> Main.gainXP(100));

            check(Main.player.level == 2, "Should reach level 2");
            check(Main.player.xp == 0, "Remaining XP should be zero");
            check(Main.player.xpToNextLevel == 150,
                    "Next level target should be 150");
            check(Main.player.maxHP == 135, "Max HP should increase by 15");
            check(Main.player.attack == 18, "Attack should increase by 3");
            check(Main.player.mana == 20, "Mana should increase by 10");
            check(Main.player.hp == 135, "Level up should restore HP");
        });

        runTest("Multiple level-ups preserve remaining XP", () -> {
            reset("Warrior");

            captureOutput(() -> Main.gainXP(250));

            check(Main.player.level == 3, "Should reach level 3");
            check(Main.player.xp == 0, "Remaining XP should be zero");
            check(Main.player.xpToNextLevel == 200,
                    "Next level target should be 200");
            check(Main.player.maxHP == 150, "Max HP should be 150");
            check(Main.player.attack == 21, "Attack should be 21");
        });

        runTest("Rest restores HP and charges gold", () -> {
            reset("Warrior");
            Main.player.hp = 50;

            captureOutput(Main::rest);

            check(Main.player.hp == 120, "HP should be fully restored");
            check(Main.player.gold == 20, "Rest should cost 10 gold");
        });

        runTest("Rest at full health costs nothing", () -> {
            reset("Warrior");

            String output = captureOutput(Main::rest);

            check(Main.player.hp == 120, "HP should remain full");
            check(Main.player.gold == 30, "Gold should not change");
            check(output.contains("already at full health"),
                    "Should explain that HP is full");
        });

        runTest("Rest without enough gold changes nothing", () -> {
            reset("Warrior");
            Main.player.hp = 50;
            Main.player.gold = 0;

            String output = captureOutput(Main::rest);

            check(Main.player.hp == 50, "HP should not change");
            check(Main.player.gold == 0, "Gold should not change");
            check(output.contains("don't have enough"),
                    "Should report insufficient gold");
        });

        // Complete game runs using simulated keyboard input
        runTest("Game starts and quits normally", () -> {
            String output = runGame("Tester\n1\n5\n");

            check(output.contains("THE FORGOTTEN KINGDOM"),
                    "Game title should appear");
            check(output.contains("Welcome, Tester the Warrior!"),
                    "Character should be created");
            check(output.contains("Thanks for playing!"),
                    "Game should exit normally");
        });

        runTest("Empty character name uses Hero", () -> {
            String output = runGame("\n1\n5\n");

            check(output.contains("Welcome, Hero the Warrior!"),
                    "Empty name should default to Hero");
        });

        runTest("Invalid class selection defaults to Warrior", () -> {
            String output = runGame("Tester\n9\n5\n");

            check(output.contains("Welcome, Tester the Warrior!"),
                    "Invalid class should default to Warrior");
        });

        runTest("Potion purchase updates inventory", () -> {
            String output = runGame("Tester\n1\n2\n1\n3\n3\n5\n");

            check(output.contains("Potion purchased!"),
                    "Potion should be purchased");
            check(output.contains("Potions: 3"),
                    "Potion count should increase to 3");
            check(output.contains("Gold: 10"),
                    "Gold should decrease from 30 to 10");
        });

        runTest("Weapon purchase rejects insufficient gold", () -> {
            String output = runGame("Tester\n1\n2\n2\n3\n5\n");

            check(output.contains("Not enough gold!"),
                    "Purchase should be rejected");
        });

        runTest("Dungeon and castle enforce level requirements", () -> {
            // Create Warrior, explore, select Dungeon, then explore again
            // and select Castle. Both locations should be locked at level 1.
            String output = runGame("Tester\n1\n1\n2\n1\n3\n5\n");

            check(output.contains("Reach level 2 first!"),
                    "Dungeon should be locked");
            check(output.contains("castle gates require level 4"),
                    "Castle should be locked");
        });

        runTest("Invalid menu input is handled", () -> {
            String output = runGame("Tester\n1\nhello\n5\n");

            check(output.contains("Enter a number:"),
                    "Non-numeric input should be rejected");
            check(output.contains("Thanks for playing!"),
                    "Game should continue after invalid input");
        });

        // Final report
        System.out.println("\n==================================");
        System.out.println("             RESULTS");
        System.out.println("==================================");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));

        if (failed == 0) {
            System.out.println("\nALL TESTS PASSED!");
        } else {
            System.out.println("\nSOME TESTS FAILED.");
            System.out.println("Review the failure details above.");
        }
    }

    static void reset(String className) {
        Main.player = new Main.Player("TestHero", className);
        Main.bossDefeated = false;
        Main.random.setSeed(12345L);
    }

    static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    static void runTest(String name, TestAction action) {
        try {
            action.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Throwable error) {
            failed++;
            System.out.println("[FAIL] " + name);
            System.out.println("       " + error.getMessage());
        }
    }

    static String captureOutput(TestAction action) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer, true, "UTF-8"));
            action.run();
        } finally {
            System.setOut(original);
        }

        return buffer.toString(StandardCharsets.UTF_8);
    }

    static String runGame(String input) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
            "java", "-cp",
            System.getProperty("java.class.path"),
            "Main"
        );

        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (OutputStream stream = process.getOutputStream()) {
            stream.write(input.getBytes(StandardCharsets.UTF_8));
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (InputStream stream = process.getInputStream()) {
            stream.transferTo(buffer);
        }

        int exitCode = process.waitFor();

        check(exitCode == 0,
                "Game process exited with code " + exitCode);

        return buffer.toString(StandardCharsets.UTF_8);
    }
}