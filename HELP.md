# Description du jeu


## ICoop

Jeu inspiré du jeu [Fireboy and Watergirl](https://en.wikipedia.org/wiki/Fireboy_and_Watergirl) et plus généralement des jeux de plateforme en coopération sur un même écran.

## But du jeu
Le but du jeu est d'acceder au manoir qui se trouve au spawn.

Pour pouvoir accéder au manoir, il va falloir faire traverser aux deux personnages les différentes aires du jeu. Chaque aire est composée de plusieurs niveaux. 

Chaque aire est dotés d'ennemies et d'obstacles qui feront subir des dégats de feu ou d'eau aux personnages.

Le personnage rouge peut intéragir avec les objets et obstacles de feu et le personnage bleu avec les objets et obstacles d'eau.

Les deux jours doivent coopérer pour traverser les niveaux et les aires.

## Lancement du jeu

Le programme principal à lancer est `ch.epfl.cs107.Play`

## Contrôles

### Joueur 1 (Rouge)
- Déplacement : ```W``` (haut), ```A``` (gauche), ```S``` (bas), ```D``` (droite)
- Utiliser l'objet en face de soi/l'objet courant dans l'inventaire: ```E```
- Changer l'objet courant dans l'inventaire: ```Q```

### Joueur 2 (Bleu)
- Déplacement : ```I``` (haut), ```J``` (gauche), ```K``` (bas), ```L``` (droite)
- Utiliser l'objet en face de soi/l'objet courant dans l'inventaire: ```O```
- Changer l'objet courant dans l'inventaire: ```U```

##  Solutions

### Spawn
Les deux joueurs apparaissent dans une salle avec 2 portes. La porte en bas a gauche mène au Labyrinthe et la porte en haut a droite mène au chemin des orbes.

Le joueur rouge et le joueur bleu doivent d'abord se rendre dans la salle des orbes pour récupérer respectivement les orbes d'eau et de feu qui permettront à chacun de devenir invincible face aux obstacles de leur élément.

### Chemin des orbes
Un mur de feu et un mur d'eau bloquent l'accès aux orbes. Les joueurs doivent chacun leur tour se positionner sur une plaque de pression pour retirer les murs et laisser l'autre joueur récupérer l'orbe.

### Labyrinthe
Le labyrinthe est un chemin jonché d'ennemies et d'obstacles. Les joueurs doivent coopérer pour traverser les obstacles et les ennemies.

Certaines parties du labyrinthe sont bloquées par des murs de feu ou d'eau qui sont pour certains désactivables par des plaques de pressions.

Les joueurs doivent avancer jusqu'au bout du labyrinthe pour récuperer un bâton de feu et un bâton d'eau qui leur permettront de détruire les murs dans l'arène.

### Arène
L'arène est une salle rempli de rochers et de murs. Les joueurs doivent détruire les rochers pour se frayer un chemin jusqu'à la clé rouge et la clé bleue.

Une fois les clés récupérées, un portail apparaît et les joueurs peuvent se rendre au manoir.


