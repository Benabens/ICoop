# ICoop — cooperative 2D game 🔥💧

A two-player cooperative game in **Java**, built for **CS-107 — Introduction to
programming (EPFL)** on the **PlayEngine** game engine.
The two players embody the elemental entities **Fire 🔥** and **Water 💧** and cooperate
through exploration, item use and elemental interactions, all the way to a final boss.

## 🎮 Concept

- Two simultaneous players, each with distinct abilities and weaknesses.
- Interactions driven by elemental affinities (damage, fire/water walls, enemies).
- Collectable items with uses (keys, orbs, explosive bombs…).
- Final boss with ranged attacks and a conditional weak spot.

## 🧠 Programming concepts applied

- **Double dispatch** through `ICoopInteractionVisitor`.
- **Polymorphism** across entities (players, projectiles, enemies, walls).
- **Per-cell behaviour** (`ICoopBehavior`): `canWalk` / `canFly` distinction.
- **State management**: inventory, immunity, active element, health.

## 🗂️ Project structure

```
ICoop/
├── game-engine/   # PlayEngine game engine (provided by the course)
├── iccoop/        # ICoop game code
│   └── src/main/
│       ├── java/ch/epfl/cs107/icoop/   # actor/, area/, handler/, ICoop.java …
│       └── resources/                  # images, sprites, sounds, dialogues
├── tutos/         # Introductory engine tutorials
├── CONCEPTION.md  # Design document
├── HELP.md        # Help / game controls
└── pom.xml        # Maven configuration (multi-module)
```

## 🚀 Running the game

Maven project. From the repository root:

```bash
mvn -q compile
mvn -q exec:java -pl iccoop -Dexec.mainClass=ch.epfl.cs107.icoop.ICoop
```

Or open the folder in IntelliJ IDEA and run the `ICoop` class of the `iccoop` module.
See `HELP.md` for both players' controls.

## ✨ Extensions implemented

- Complete final boss (`HellSkull`).
- Extended map and new playable areas.
- Advanced wall / element interactions.
- Projectiles that cross impassable areas (`canFly`).

## 👥 Authors

Built in a pair for EPFL's CS-107 course.
