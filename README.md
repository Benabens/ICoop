# ICoop — Jeu 2D coopératif 🔥💧

Jeu coopératif en **Java** pour deux joueurs, développé dans le cadre du cours
**CS-107 — Introduction à la programmation (EPFL)** sur le moteur **PlayEngine**.
Deux joueurs incarnent des entités élémentaires **Feu 🔥** et **Eau 💧** et coopèrent
à travers l'exploration, l'usage d'objets et des interactions élémentaires jusqu'à un boss final.

## 🎮 Concept

- Deux joueurs simultanés, avec des capacités et vulnérabilités distinctes.
- Interactions basées sur les affinités élémentaires (dégâts, murs de feu/eau, ennemis).
- Objets collectables avec usages (clés, orbes, bombes explosives…).
- Boss final avec attaques à distance et vulnérabilité conditionnelle.

## 🧠 Concepts de programmation appliqués

- **Double dispatch** via `ICoopInteractionVisitor`.
- **Polymorphisme** sur les entités (joueurs, projectiles, ennemis, murs).
- **Behavior par cellule** (`ICoopBehavior`) : distinction `canWalk` / `canFly`.
- **Gestion d'état** : inventaire, immunité, élément actif, santé.

## 🗂️ Structure du projet

```
ICoop/
├── game-engine/   # Moteur de jeu PlayEngine (fourni par le cours)
├── iccoop/        # Code du jeu ICoop
│   └── src/main/
│       ├── java/ch/epfl/cs107/icoop/   # actor/, area/, handler/, ICoop.java …
│       └── resources/                  # images, sprites, sons, dialogues
├── tutos/         # Tutoriels d'introduction au moteur
├── CONCEPTION.md  # Document de conception
├── HELP.md        # Aide / commandes du jeu
└── pom.xml        # Configuration Maven (multi-modules)
```

## 🚀 Lancer le jeu

Projet Maven. Depuis la racine :

```bash
mvn -q compile
mvn -q exec:java -pl iccoop -Dexec.mainClass=ch.epfl.cs107.icoop.ICoop
```

Ou ouvrir le dossier dans IntelliJ IDEA et exécuter la classe `ICoop` du module `iccoop`.
Voir `HELP.md` pour les contrôles des deux joueurs.

## ✨ Extensions réalisées

- Boss final complet (`HellSkull`).
- Extension de la carte et nouvelles zones jouables.
- Interactions avancées murs / éléments.
- Projectiles traversant les zones non franchissables (`canFly`).

## 👥 Auteurs

Projet réalisé en binôme dans le cadre du cours CS-107 de l'EPFL.
