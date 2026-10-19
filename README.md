
# Forgotten Kingdom ⚔️

Forgotten Kingdom is a text-based RPG adventure built entirely with Java. Create a character, explore dangerous locations, fight monsters, earn gold and experience, and level up to become stronger.

## Features 📜

- Character creation with three playable classes.
- Turn-based combat with attacks, special abilities, potions, and fleeing.
- Multiple locations to explore.
- Experience and leveling system.
- Shop for health potions and weapon upgrades.
- Rest system to restore health.
- Gold and inventory management.
- Automated tests for game mechanics.

## Character Classes

Each class has different starting stats and a unique ability.

| Class | Health | Attack | Mana |
|---|---:|---:|---:|
| Warrior | 120 | 15 | 10 |
| Mage | 80 | 10 | 50 |
| Rogue | 100 | 13 | 20 |

- **Warrior:** A durable fighter with high health and the Power Strike ability.
- **Mage:** A magic user with high mana who can cast Fireball.
- **Rogue:** A quick fighter with a chance to deal critical damage and the Quick Strike ability.

## How to Play

When the game starts, enter your character's name and choose a class. Use the main menu to select an action.

1. **Explore** — Visit locations and encounter enemies.
2. **Shop** — Purchase health potions and weapon upgrades.
3. **View Character** — Check your character's stats and progress.
4. **Rest** — Restore your health by spending gold.
5. **Quit** — Exit the game.

Defeat enemies to earn experience points (XP) and gold. Earn enough XP to level up and improve your character's stats.

### Locations

| Location | Level Requirement |
|---|---|
| Whispering Forest | Available from the start |
| Abandoned Dungeon | Level 2 |
| Shadow Castle | Level 4 |

Manage your health, mana, potions, and gold carefully as you explore increasingly dangerous areas.

## Combat System

Combat is turn-based. During an encounter, you can choose to:

- **Attack:** Deal damage with a normal attack.
- **Use Ability:** Use your character class's special ability.
- **Use Potion:** Restore health using a potion.
- **Flee:** Attempt to escape from a regular encounter.

Boss encounters cannot be escaped. If you defeat an enemy, you receive experience and gold. Rogue attacks can also deal critical damage.

## Leveling System

Defeating enemies rewards you with experience points. When you earn enough XP, your character levels up.

Leveling up increases your maximum health, attack power, and maximum mana, and restores your health. The amount of experience needed for the next level increases as you progress.

## Shop and Rest System

Gold can be used to prepare for future battles.

| Item | Cost | Effect |
|---|---:|---|
| Health Potion | 20 gold | Adds a potion to your inventory |
| Weapon Upgrade | 60 gold | Increases attack power |
| Rest | 10 gold | Restores health to maximum |

You must have enough gold to make a purchase or rest. Resting at full health does not cost gold.

## Requirements

- Java Development Kit (JDK)
- A terminal or command prompt

Check that Java is installed by running:

```bash
java -version
javac -version
```

Both commands should display a version number.

## How to Run ▶️

### 1. Clone the Repository

Copy your repository's URL from GitHub and run the following command in your terminal:

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/ForgottenKingdom.git
```

Replace `YOUR_GITHUB_USERNAME` with your GitHub username.

### 2. Open the Project Folder

Move into the downloaded project folder:

```bash
cd ForgottenKingdom
```

### 3. Compile the Java Files

Compile the game and test files:

```bash
javac Main.java MainTest.java
```

### 4. Start the Game

Run the game:

```bash
java Main
```

Follow the instructions displayed in the terminal to begin your adventure.

## Running the Tests

The project includes `MainTest.java`, which checks important game mechanics, including character stats, combat damage, mana usage, leveling, shop purchases, and location requirements.

Compile the files if you have not already:

```bash
javac Main.java MainTest.java
```

Run the tests:

```bash
java MainTest
```

The tests run separately from the game and do not require an external testing framework.

## Project Structure

```text
ForgottenKingdom/
├── Main.java       # Main game and gameplay logic
├── MainTest.java   # Automated tests
├── .gitignore      # Files Git should ignore
└── README.md       # Project documentation
```

## Technologies Used

- **Java** — Main programming language.
- **Scanner** — Reads player input.
- **Random** — Adds variation to combat damage.
- **Git** — Tracks changes to the project.
- **GitHub** — Hosts the project repository.

---

Enjoy your adventure in the Forgotten Kingdom!