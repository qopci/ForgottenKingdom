
import java.util.Random;
import java.util.Scanner;

public class Main {

    static final Scanner scanner = new Scanner(System.in);
    static final Random random = new Random();

    static Player player;
    static boolean bossDefeated = false;

    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("      THE FORGOTTEN KINGDOM");
        System.out.println("==================================");
        System.out.println("A kingdom has fallen into darkness.");
        System.out.println("Defeat the monsters and save the realm.\n");

        createCharacter();

        boolean playing = true;

        while (playing && player.hp > 0 && !bossDefeated) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Explore the kingdom");
            System.out.println("2. Visit the shop");
            System.out.println("3. View character");
            System.out.println("4. Rest (restore some HP)");
            System.out.println("5. Quit game");
            System.out.print("Choose an action: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    explore();
                    break;
                case 2:
                    visitShop();
                    break;
                case 3:
                    showCharacter();
                    break;
                case 4:
                    rest();
                    break;
                case 5:
                    playing = false;
                    System.out.println("You leave the kingdom... for now.");
                    break;
                default:
                    System.out.println("Choose a number from 1 to 5.");
            }
        }

        if (bossDefeated) {
            System.out.println("\n==================================");
            System.out.println("       THE KINGDOM IS SAVED!");
            System.out.println("==================================");
            System.out.println("Congratulations, " + player.name + "!");
        } else if (player.hp <= 0) {
            System.out.println("\nYou have fallen in battle...");
            System.out.println("Your adventure ends here.");
        }

        System.out.println("\nThanks for playing!");
        scanner.close();
    }

    // Create a character and select a class.
    static void createCharacter() {
        System.out.print("Enter your hero's name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            name = "Hero";
        }

        System.out.println("\nChoose your class:");
        System.out.println("1. Warrior - high health and powerful attacks");
        System.out.println("2. Mage    - powerful magic and lower health");
        System.out.println("3. Rogue   - balanced stats and critical hits");
        System.out.print("Class: ");

        int choice = readInt();
        String className;

        switch (choice) {
            case 2:
                className = "Mage";
                break;
            case 3:
                className = "Rogue";
                break;
            default:
                className = "Warrior";
        }

        player = new Player(name, className);

        System.out.println("\nWelcome, " + player.name + " the "
                + player.className + "!");
        System.out.println("Your adventure begins...");
    }

    // Explore locations and encounter events.
    static void explore() {
        System.out.println("\nWhere would you like to go?");
        System.out.println("1. Whispering Forest");
        System.out.println("2. Abandoned Dungeon");
        System.out.println("3. Shadow Castle");
        System.out.println("4. Return to menu");
        System.out.print("Destination: ");

        int choice = readInt();

        switch (choice) {
            case 1:
                System.out.println("\nYou enter the Whispering Forest...");
                encounter("Goblin", 35, 8, 15, 20);
                break;

            case 2:
                if (player.level < 2) {
                    System.out.println(
                            "The dungeon is dangerous. Reach level 2 first!");
                    return;
                }
                System.out.println("\nYou descend into the Abandoned Dungeon...");
                encounter("Skeleton Knight", 55, 12, 25, 35);
                break;

            case 3:
                if (player.level < 4) {
                    System.out.println(
                            "The castle gates require level 4.");
                    return;
                }
                if (bossDefeated) {
                    System.out.println("The kingdom is already saved!");
                    return;
                }
                System.out.println("\nYou enter the Shadow Castle...");
                encounter("The Shadow King", 140, 18, 100, 100);
                break;

            case 4:
                return;

            default:
                System.out.println("That destination doesn't exist.");
        }
    }

    // Start a turn-based battle.
    static void encounter(String monsterName, int monsterHP,
                          int monsterAttack, int xpReward, int goldReward) {

        Monster monster = new Monster(
                monsterName, monsterHP, monsterAttack);

        System.out.println("\nA " + monster.name + " appears!");
        System.out.println("Enemy HP: " + monster.hp);

        while (player.hp > 0 && monster.hp > 0) {
            System.out.println("\n------------------------------");
            System.out.println(player.name + " HP: "
                    + player.hp + "/" + player.maxHP);
            System.out.println(monster.name + " HP: " + monster.hp);
            System.out.println("------------------------------");

            System.out.println("1. Attack");
            System.out.println("2. Use class ability");
            System.out.println("3. Drink health potion");
            System.out.println("4. Flee");
            System.out.print("Action: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    normalAttack(monster);
                    break;

                case 2:
                    useAbility(monster);
                    break;

                case 3:
                    if (player.potions > 0) {
                        player.potions--;
                        int healed = Math.min(
                                35, player.maxHP - player.hp);
                        player.hp += healed;
                        System.out.println("You restore " + healed
                                + " HP!");
                    } else {
                        System.out.println("You have no potions!");
                        continue;
                    }
                    break;

                case 4:
                    if (monster.name.equals("The Shadow King")) {
                        System.out.println(
                                "There is no escape from the final boss!");
                        continue;
                    }

                    if (random.nextInt(100) < 50) {
                        System.out.println("You escaped safely!");
                        return;
                    } else {
                        System.out.println("You failed to escape!");
                    }
                    break;

                default:
                    System.out.println("Invalid action!");
                    continue;
            }

            // Stop if the monster was defeated by your action.
            if (monster.hp <= 0) {
                break;
            }

            // The monster attacks after your turn.
            int damage = monster.attack + random.nextInt(5);
            player.hp = Math.max(0, player.hp - damage);

            System.out.println(monster.name + " hits you for "
                    + damage + " damage!");
        }

        if (player.hp <= 0) {
            return;
        }

        System.out.println("\nYou defeated " + monster.name + "!");
        System.out.println("You earned " + xpReward + " XP and "
                + goldReward + " gold.");

        player.gold += goldReward;
        gainXP(xpReward);

        if (monster.name.equals("The Shadow King")) {
            bossDefeated = true;
        }
    }

    // Basic attack, with a chance of a critical hit.
    static void normalAttack(Monster monster) {
        int damage = player.attack + random.nextInt(6);

        if (player.className.equals("Rogue")
                && random.nextInt(100) < 30) {
            damage *= 2;
            System.out.println("CRITICAL HIT!");
        }

        monster.hp = Math.max(0, monster.hp - damage);
        System.out.println("You deal " + damage + " damage!");
    }

    // Each class has its own special ability.
    static void useAbility(Monster monster) {
        switch (player.className) {
            case "Warrior":
                int warriorDamage = player.attack * 2
                        + random.nextInt(6);
                monster.hp = Math.max(
                        0, monster.hp - warriorDamage);
                System.out.println("POWER STRIKE! You deal "
                        + warriorDamage + " damage.");
                break;

            case "Mage":
                if (player.mana < 15) {
                    System.out.println("Not enough mana!");
                    return;
                }
                player.mana -= 15;
                int magicDamage = 25 + random.nextInt(11);
                monster.hp = Math.max(
                        0, monster.hp - magicDamage);
                System.out.println("FIREBALL! You deal "
                        + magicDamage + " magic damage.");
                break;

            case "Rogue":
                int rogueDamage = player.attack
                        + random.nextInt(16) + 5;
                monster.hp = Math.max(
                        0, monster.hp - rogueDamage);
                System.out.println("QUICK STRIKE! You deal "
                        + rogueDamage + " damage.");
                break;

            default:
                System.out.println("No ability available.");
        }
    }

    // Gain XP and level up when the requirement is reached.
    static void gainXP(int amount) {
        player.xp += amount;

        while (player.xp >= player.xpToNextLevel) {
            player.xp -= player.xpToNextLevel;
            player.level++;

            player.xpToNextLevel += 50;
            player.maxHP += 15;
            player.attack += 3;
            player.hp = player.maxHP;
            player.mana += 10;

            System.out.println("\n*** LEVEL UP! ***");
            System.out.println("You are now level " + player.level + "!");
            System.out.println("Attack increased by 3.");
            System.out.println("Maximum HP increased by 15.");
            System.out.println("Your health has been restored!");
        }
    }

    // Buy useful items using earned gold.
    static void visitShop() {
        boolean shopping = true;

        while (shopping) {
            System.out.println("\n========== SHOP ==========");
            System.out.println("Gold: " + player.gold);
            System.out.println("1. Health potion (+35 HP) - 20 gold");
            System.out.println("2. Permanent weapon upgrade (+5 attack) - 60 gold");
            System.out.println("3. Return");
            System.out.print("Buy: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    if (player.gold >= 20) {
                        player.gold -= 20;
                        player.potions++;
                        System.out.println("Potion purchased!");
                        System.out.println("Potions: " + player.potions);
                    } else {
                        System.out.println("Not enough gold!");
                    }
                    break;

                case 2:
                    if (player.gold >= 60) {
                        player.gold -= 60;
                        player.attack += 5;
                        System.out.println("Weapon upgraded!");
                        System.out.println("Attack: " + player.attack);
                    } else {
                        System.out.println("Not enough gold!");
                    }
                    break;

                case 3:
                    shopping = false;
                    break;

                default:
                    System.out.println("Choose 1, 2, or 3.");
            }
        }
    }

    // Display the current character statistics.
    static void showCharacter() {
        System.out.println("\n======= CHARACTER =======");
        System.out.println("Name: " + player.name);
        System.out.println("Class: " + player.className);
        System.out.println("Level: " + player.level);
        System.out.println("HP: " + player.hp + "/" + player.maxHP);
        System.out.println("Attack: " + player.attack);
        System.out.println("Mana: " + player.mana);
        System.out.println("XP: " + player.xp + "/"
                + player.xpToNextLevel);
        System.out.println("Gold: " + player.gold);
        System.out.println("Potions: " + player.potions);
        System.out.println("Final boss defeated: "
                + (bossDefeated ? "Yes" : "No"));
    }

    // Restores health, but costs some gold.
    static void rest() {
        if (player.hp == player.maxHP) {
            System.out.println("You're already at full health!");
            return;
        }

        int cost = 10;

        if (player.gold < cost) {
            System.out.println("Resting costs 10 gold. You don't have enough.");
            return;
        }

        player.gold -= cost;
        int healed = player.maxHP - player.hp;
        player.hp = player.maxHP;

        System.out.println("You rest at the inn and recover "
                + healed + " HP.");
        System.out.println("You spent " + cost + " gold.");
    }

    // Safely read an integer from terminal input.
    static int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a number: ");
            scanner.next();
        }

        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    // Player class: stores the hero's stats and inventory.
    static class Player {
        String name;
        String className;

        int level = 1;
        int hp;
        int maxHP;
        int attack;
        int mana;
        int xp = 0;
        int xpToNextLevel = 100;
        int gold = 30;
        int potions = 2;

        Player(String name, String className) {
            this.name = name;
            this.className = className;

            switch (className) {
                case "Mage":
                    maxHP = 80;
                    attack = 10;
                    mana = 50;
                    break;

                case "Rogue":
                    maxHP = 100;
                    attack = 13;
                    mana = 20;
                    break;

                default:
                    maxHP = 120;
                    attack = 15;
                    mana = 10;
                    break;
            }

            hp = maxHP;
        }
    }

    // Monster class: stores enemy stats.
    static class Monster {
        String name;
        int hp;
        int attack;

        Monster(String name, int hp, int attack) {
            this.name = name;
            this.hp = hp;
            this.attack = attack;
        }
    }
}