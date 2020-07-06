
# ITI 1521 - Lab 09 - Entrées-sorties (E/S) en Java, Message secret et Exceptions

## Submission

Lire le [junit instructions](JUNIT.fr.md) au besoin pour aider
avec les tests du lab.

Lire le [guides de soumission](SUBMISSION.fr.md) avec attention.
Les erreurs de soumission vont affecté votre évaluation.

Soumettez les réponses aux

* SecretMessage.java
* PlayList.java
* Song.java
* SortByAlbum.java
* SortByArtist.java
* SortByName.java

## Objectifs d'apprentissage

* **Concevoir** un programme Java pour lire des données du clavier ou d'un fichier texte.
* **Concevoir** un programme Java pour écrire des données sur la sortie ou dans un fichier texte.
* **Modifier** un programme Java existant afin de déclarer toutes les exceptions à déclaration obligatoire.

Ce laboratoire comporte plusieurs parties.

## 1. Entrées-sorties (E/S) en Java

La première partie introduit les concepts de base des entrées-sorties qui seront nécessaires pour réaliser ce laboratoire (conservez ces notes et ces exemples, ça vous sera utile pour le cours structures de données). La seconde partie consiste à modifier l’application **PlayListManager** afin de lire/écrire les chansons à partir de fichiers.

Ce document présente les éléments de base pour faire des entrées-sorties (E/S) en Java. Il couvre un sous-ensemble des classes du package java.io. Depuis la version 1.4 de Java, il y a un nouveau package, java.nio (new io), définissant des concepts plus avancés d'E/S tels que les «buffers», «channels», et «memory mapping», ces sujets ne seront pas couverts ici.

Les entrées-sorties en Java semblent assez complexes à première vue. Tout d'abord, il y a un très grand nombre de classes. Ensuite, il faut combiner plusieurs objets pour réaliser les traitements. Pourquoi cette complexité ? Java est un langage moderne, développé au milieu des années 1990s alors que le Web allait devenir une réalité. Ainsi, les données peuvent être lues et écrites à partir de plusieurs sources, dont le clavier, la console, des disques externes, mais aussi le réseau. De plus, la présence du Web a aussi stimulé la création de classes permettant l'internationalisation de programmes (pour les postes de travail anglophones, pour les langues européennes, mais aussi arabes et orientales) ainsi que le traitement de données multimédia.

### 1.1 Définitions

Un **flux** (stream) est une séquence ordonnée de données ayant une source ou une destination. Il y a deux genres de flux : les **flux de caractères** (character streams) et les **flux d'octets** (byte streams).

Java utilise des Unicodes pour encoder les caractères — les flux de caractères sont en général associés aux entrées-sorties de textes (donc lisible par l'humain). Les flux de caractères s'appellent readers et writers. Ce document traite principalement de ces types de flux. Les flux d'octets (byte streams) sont associés aux entrées-sorties de données (binaires). Les fichiers audio et vidéo, jpeg et mp3, en sont des exemples. Les informations peuvent être lues ou écrites sur un support externe. Pour chaque flux en lecture (ou reader), il existe un flux en écriture (ou writer) correspondant. Il existe aussi un troisième mode d'accès, le mode direct, permettant à la fois les lectures et écritures. Le mode direct n'est pas traité ici.

### 1.2 Exposé général

Il existe deux genres de flux et trois modes d'accès.

* **Flux :** caractères ou octets ;
* **Accès :** lecture, écriture ou direct.

De plus, le support utilisé (clavier, console, disque, mémoire, réseau, etc.) impose aussi ses propres contraintes (faut-il un tampon (buffer) ou pas, par exemple). Le package java.io comporte quelque 50 classes, 10 interfaces et plus de 15 exceptions. Le grand nombre de classes pourrait à lui seul intimider les nouveaux programmeurs. Pour ajouter à cette complexité, il faut généralement combiner des objets de deux ou trois classes afin d'effectuer quelque traitement que ce soit, comme le démontre cet exemple.

```java
InputStreamReader in = new InputStreamReader(new FileInputStream("data"));
```

Ici “data” est le nom d'un fichier d'entrée. Les sections qui suivent passent en revue les principaux concepts liés aux entrées-sorties en Java. La majorité des concepts sont accompagnés d'exemples et d'exercices. Compilez et exécutez tous les exemples. Complétez tous les exercices.

### 1.3 Flux

**InputStream** et **OutputStream** sont deux classes abstraites définissant les méthodes communes aux flux d'entrée et de sortie.

#### 1.3.1 InputStream

La classe **InputStream** déclare les trois méthodes suivantes.

* **int read() :** Lit le prochain octet du flux d'entrée. L'octet lu est retourné dans un entier, intervalle 0 à 255\. Si aucun octet n'est disponible, signifiant que la fin du flux a été atteinte, alors la méthode retourne la valeur -1\.
* **int read( byte[] b ) :** Lit plusieurs octets à la fois. Les octets sont sauvegardés dans le tampon (buffer, un tableau) b. La méthode retourne le nombre d'octets lus.
* **close() :** Fermeture du flux d'entrée. Libère les ressources qui lui sont associées.

La classe **InputStream** est abstraite. Voici des exemples de ses sous-classes : **AudioInputStream, ByteArrayInputStream, FileInputStream, FilterInputStream, ObjectInputStream, PipedInputStream, SequenceInputStream** et **StringBufferInputStream**. Parmi celles-ci, la classe **FileInputStream** sera présentée ci-bas. Cette classe permet la lecture d'octets à partir d'un fichier.

#### 1.3.2 OutputStream

La classe abstraite **OutputStream** déclare les méthodes qui suivent.

* **write( byte[] b ) :** Écrit b.length octets sur la sortie.
* **flush() :** Vide la mémoire tampon du flux, forçant ainsi l'écriture de tous les octets se trouvant encore dans le tampon.
* **close() :** Fermeture du flux. Libère les ressources associées à ce flux.

La classe OutputStream est abstraite. Voici des exemples de sous-classes concrètes : **ByteArrayOutputStream, FileOutputStream, FilterOutputStream, ObjectOutputStream et PipedOutputStream**. Parmi celles-ci, la classe **FileOutputStream** est utilisée fréquemment et sera étudiée ci-bas. Elle permet l'écriture de l'octet dans un fichier.

#### 1.3.3 System.in et System.out

Deux objets sont prédéfinis par le système. **System.in** est un flux d'entrée, généralement associé au clavier. **System.out** est un flux de sortie, généralement associé à la console.

### 1.4 Étapes

L'écriture dans (ou la lecture depuis) un fichier nécessite en général trois étapes :

* Ouvrir le fichier
* Écriture (ou lecture)
* Fermer le fichier

Il est important de toujours fermer les fichiers afin que les données (possiblement sauvegardées dans un tampon d'écriture) soient sauvegardées dans le fichier, mais aussi afin de libérer les ressources internes et externes associées.

### 1.5 Lecture

Limitons la portée de cette discussion à la lecture à partir d'un fichier ou la lecture à partir du clavier.

#### 1.5.1 Lecture à partir d'un fichier

Afin de lire des données d'un fichier, il faut créer un objet FileInputStream. Attardons-nous aux deux constructeurs suivants.

* **FileInputStream( String name ) :** Ce constructeur reçoit le nom du fichier en paramètre. Exemple :

```java
InputStream in = new FileInputStream("data");
```

* **FileInputStream( File file ) :** Ce constructeur reçoit en paramètre un objet **File**, un objet représentant le fichier externe.

```java
File f = new File("data");
InputStream in = new FileInputStream(f);
```

L'objet **File** permet d'effectuer toutes sortes d'opérations sur le fichier. Voici quelques exemples.

```java
f.delete();
f.exists();
f.getName();
f.getPath();
f.length();
```

**FileInputStream** est une sous-classe d'**InputStream**. Tout comme son parent, cette classe ne lit que des octets.

#### 1.5.2 InputStreamReader

La classe **InputStreamReader** sert de passerelle entre un flux d'octets et un flux de caractères. On l'utilise comme suit.

```java
InputStreamReader in = new InputStreamReader(new FileInputStream("data"));
```

ou encore,

```java
InputStreamReader in = new InputStreamReader(System.in);
```

L'objet **System.in** est généralement associé au clavier du poste de travail.

* **int read() :** Lecture d'un caractère. Retourne -1 lorsque la fin de l'entrée est atteinte (end-of-file (eof), end-of-stream (eos)). L'entier doit être converti en caractère.

```java
int i = in.read();
if (i != -1) {
  char c = (char) i;
}
```

Voir

* Unicode.java
* Keyboard.java

* **int read( char [] b ) :** Lit plusieurs caractères à la fois. Les caractères sont mis dans le tableau **b**. La méthode retourne le nombre de caractères lus ou **-1** si la fin est atteinte.

```java
char[] buffer = new char[ 256 ];
num = in.read(buffer);
String str = new String(buffer);
```

## Exercice 1 :

Concevez une classe afin de lire des caractères au clavier utilisant la méthode **read( char[] b )** ; le nombre de caractères lus est déterminé par la taille du tableau (tampon). Utilisez la classe **Keyboard** comme point de départ. Voici une liste des modifications majeures que vous devrez faire.

1.  Vous n'avez plus besoin de la variable i pour stocker les valeurs lues. Vous utiliserez plutôt un _buffer_ de type **char[]** tel que vu précédemment.
2.  Vous devez changez la condition de votre boucle while. Notamment, la variable i ne fait plus partie de la solution, tandis que vous utiliserez une méthode **read( char[] b )** à la place.
3.  Une fois la lecture faite par la méthode read, l'entrée est stockée dans votre variable tampon (buffer). Vous devez alors la convertir en chaîne de caractères (String).
4.  Sachez que certains symboles ne sont pas affichables. Vous devez utiliser la méthode trim afin de retirer les caractères non affichables. Notamment, **votreString.trim()** retourne un nouvel objet de type String sans ces caractères non affichables
5.  Imprimez maintenant la chaîne de caractère que vous avez obtenue à la console.
6.  Finalement, vider votre tampon avec la commande suivante: **Arrays.fill( buffer, '\u0000' );**

Faites quelques tests. Peu importe le nombre de caractères lus, la longueur de la chaîne est toujours 256\.

* Keyboard.java

#### 1.5.3 BufferedReader

Certaines applications doivent lire les données **ligne par ligne**. Pour ces applications, nous utiliserons un (objet) **BufferedReader**. BufferedReader utilise un objet de la classe **InputStreamReader** afin de lire les données. Ce dernier, InputStreamReader utilise InputStream afin de lire les octets. Chaque couche (objet) ajoute de nouvelles fonctions. **InputStreamReader convertit les octets en caractères**. Finalement, **BufferedReader regroupe les caractères en chaînes de caractères**, par exemple une ligne à la fois.

```java
FileInputStream f = FileInputStream("data");
InputStreamReader is = new InputStreamReader(f);
BufferedReader in = new BufferedReader(is);
```

ou

```java
BufferedReader in = new BufferedReader(
new InputStreamReader(
new FileInputStream("data")));
String s = in.readLine();
```

La classe **Copy** est un programme qui copie le contenu d'un fichier à votre console.

Notez que le traitement des exceptions est omis dans cet exemple.

```java
import java.io.*;

public class Copy {

  public static void copy(String fileName)
    throws IOException, FileNotFoundException {

    InputStreamReader input;

    input = new InputStreamReader(new FileInputStream(fileName)); //open file

    int c;
    //we read character by character
    while ((c = input.read()) != -1) {

      //prints on the console
      System.out.write(c);

    }

    //close the opened file
    input.close();
  }

  public static void main(String[] args)
    throws IOException, FileNotFoundException {

    if (args.length != 1) {
      System.out.println("Usage: java Copy file");
      System.exit(0);
    }

    copy(args[0]);
  }
}
```

Vous pouvez remarquez que le programme **Copy** copie le contenu du fichier donné en paramètre un caractère à la fois, et ce, jusqu'à ce que la fin du flux de données (eof: end of stream), -1, soit lue par le **InputStreamReader**. Les trois étapes présentées précédemment y sont aussi respectées, c'est-à-dire l'ouverture du fichier, la lecture, et sa fermeture.

* Copy.java

## Exercice 2 :

Créez un programme affichant toutes les lignes d'un fichier contenant un certain mot. Affichez aussi le numéro de la ligne.

* Vous devrez utiliser la méthode **readLine()** de la classe **BufferedReader**. Cette méthode retourne sous forme de chaîne de caractères (un String) le contenu de la ligne ou null si nous avons atteint la fin du fichier.
* Vous devrez ensuite vérifier si le mot donné fait partie du String. Pour ce faire, utilisez la méthode indexOf(votreMot) qui vous retournera la position de votre mot dans la chaîne de caractères ou -1 s'il n'en fait pas partit.

Voici le code à partir duquel vous pouvez résoudre le problème.

* Find.java

## Exercice 3 :

Modifier le code de l'exercice précédent afin de calculer le nombre de fois que se trouvent le mot **Ottawa** dans le fichier suivant :

* Ottawa.txt

**Indice**: une fois que vous avez trouvé le premier index du mot Ottawa dans le String, vous pouvez diviser votre String en utilisant a méthode de String **substring(int index)** qui retourne un nouvel objet de type String contenant la chaîne de caractère commençant par le caractère la position du paramètre index et finissant à la fin du String original.

Plutôt qu’un fichier, il est possible d’utiliser une adresse URL comme source. Dans ce cas il faut importer « java.net.* », puis créer un nouvel objet URL. Lorsque l’on crée le InputStreamReader, il faut aussi s’assurer d’appeler la méthode openStream de l’objet URL pour que l’on puisse lire l’information.

```java
URL address = new URL("http://www.google.ca");
InputStreamReader is = new InputStreamReader(address.openStream());
```

## Exercice 4 :

Implémentez une classe afin de télécharger et afficher le contenu d'une page Web. Essayez-la avec plusieurs adresses, notamment avec la page web de ce laboratoire. Comme vous n'utiliserez pas de fichiers pour cet exercice, l'exception **FileNotFoundException** ne fera pas partie de la déclaration des méthodes parmi les méthodes qui peuvent être lancées (throws).

Notez toutefois que l'usage d'objet de la classe **URL** peut lancer l'exception **MalformedURLException**.

### 1.6 Écriture

Considérons maintenant l'écriture sur sortie standard ainsi que l'écriture dans un fichier. Vous remarquerez la similarité avec la lecture.

#### 1.6.1 Écrire dans un fichier

Afin d'écrire dans un fichier, nous utiliserons un (objet) FileOutputStream. Voici deux constructeurs.

* **FileOutputStream( String name ) :** Crée un flux de sortie pour l'écriture dans un fichier nommé **name**.

```java
OutputStream out = new FileOutputStream("data");
```

* **FileOutputStream( File file ) :** Ce constructeur reçoit un objet File.

```java
File f = new File( "data" );
OutputStream out = new FileOuputStream( f );
```

Tout comme son parent, OutputStream, cette classe ne sert qu'à l'écriture d'octets.

* **OutputStreamWriter :** Une passerelle pour la conversion de caractères en octets.

```java
OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("data "));
```


ou

```java
OutputStreamWriter out = new OutputStreamWriter(System.out);
OutputStreamWriter err = new OutputStreamWriter(System.err);
```


Les messages d'erreurs sont en général écrits sur **System.err**, la sortie standard. Voici les méthodes de la classe **OutputStreamWriter**.

* **write( int c ) :** Écrit un seul caractère ;
* **write( char[] buffer ) :** Écrit le contenu du tableau sur la sortie ;
* **write( String s ) :** Écriture d'une chaîne de caractères.


## Exercice 5 :

Modifiez le programme Copy.java afin de spécifier un fichier destination. Ainsi, l'application copie le contenu d'un fichier d'entrée dans un fichier en sortie.

* Copy.java

* **PrintWriter :** Cette classe définit un ensemble de méthodes permettant l'écriture de valeurs d'un type primitif ou objet.

```java
print(boolean b)  : Prints a boolean value.
print(char c)     : Prints a character.
print(char[] s)   : Prints an array of characters.
print(double d)   : Prints a double-precision floating-point number.
print(float f)    : Prints a floating-point number.
print(int i)      : Prints an integer.
print(long l)     : Prints a long integer.
print(Object obj) : Prints an object.
print(String s)   : Prints a string.
```

Le méthodes suivantes affichent aussi un séparateur de lignes (le séparateur varie selon le système d'exploitation utilisé, cette difficulté est traitée pour nous par l'objet PrintWriter).

```java
println()           : Prints a line separator string.
println(boolean b)  : Prints a boolean value.
println(char c)     : Prints a character.
println(char[] s)   : Prints an array of characters.
println(double d)   : Prints a double-precision floating-point number.
println(float f)    : Prints a floating-point number.
println(int i)      : Prints an integer.
println(long l)     : Prints a long integer.
println(Object obj) : Prints an object.
println(String s)   : Prints a string.
```

### 1.7 Fichiers CSV

Les fichiers à valeurs séparées par virgules (Comma-Separated Values – CSV ) sont des fichiers de texte simple dans lesquels les données sont enregistrées colonne par colonne, et divisées par un séparateur. Le séparateur est généralement une virgule « , ». Les fichiers de format CSV peuvent être importés ou exportés d’un programme qui enregistre ces données sous forme de tableau. Un _parseur_ (analyseur) peut prendre un fichier CSV et convertir le texte CSV dans un tableau ou un objet qui sera utilisé par le programme.

Par exemple, voici un fichier CSV qui contient un bottin des indicatifs régionaux de différent pays :

```csv
"1","US","United States"
"2","MY","Malaysia"
"3","AU","Australia"
```

Dans une application simple, nous utilisons la méthode standard **split()** pour parser le fichier CSV. L’exemple suivant est un fichier CSV simple, sauvegardé dans le fichier "/Users/admin/csv/country.csv":

```csv
"1.0.0.0","1.0.0.255","16777216","16777471","AU","Australia"
"1.0.1.0","1.0.3.255","16777472","16778239","CN","China"
"1.0.4.0","1.0.7.255","16778240","16779263","AU","Australia"
"1.0.8.0","1.0.15.255","16779264","16781311","CN","China"
"1.0.16.0","1.0.31.255","16781312","16785407","JP","Japan"
"1.0.32.0","1.0.63.255","16785408","16793599","CN","China"
"1.0.64.0","1.0.127.255","16793600","16809983","JP","Japan"
"1.0.128.0","1.0.255.255","16809984","16842751","TH","Thailand"
```

Pour parser le fichier CSV, il suffit de lire le fichier, puis de le diviser par le séparateur (la virgule « , ») en utilisant **line.split(",")**. Vous pouvez ensuite prendre le texte parser et l’utiliser ou le formater comme vous le désirez.

```java
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

  public static void main(String[] args) {

    String csvFile = "/Users/admin/csv/country.csv";
    BufferedReader br = null;
    String line = "";

    try {

      br = new BufferedReader(new FileReader(csvFile));
      while ((line = br.readLine()) != null) {
        // use comma as separator
        String[] country = line.split(",");

        System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
```

Exécuter le code si dessus nous donne une possibilité de formatage des données, voici la sortie :

```java
Country [code= "AU" , name="Australia"]
Country [code= "CN" , name="China"]
Country [code= "AU" , name="Australia"]
Country [code= "CN" , name="China"]
Country [code= "JP" , name="Japan"]
Country [code= "CN" , name="China"]
Country [code= "JP" , name="Japan"]
Country [code= "TH" , name="Thailand"]
```

Lorsque l’on parse des fichiers CSV, il y a deux problèmes courants :

1.  Le séparateur est aussi contenu dans les données. Par exemple : "aaa","b,bb","ccc". Dans ce cas, la virgule « , » est le séparateur, mais elle apparait aussi dans la donne **b,bb** que nous ne voulons pas séparer.
2.  Les guillemets anglais « " » sont utilisés pour contenir les données, mais les données contiennent aussi des guillemets anglais. Par exemple : "aaa","b""bb","ccc". Dans ce cas, les guillemets sont contenus dans la donnée **b""bb**. Notez que pour que le guillemet apparaisse dans la donnée, il doit être échappé en la précédant par un autre guillemet.

Pour ces deux problèmes courants, il existe des solutions plus avancées qui sont requises pour parser les fichiers CSV formatés avec des données contenants des séparateurs ou des guillemets.

### Rappel: Les exceptions

Cette section revisite quelques concepts liés aux traitements des exceptions en Java et présente ceux qui sont spécifiques aux traitements des entrées-sorties.

#### IOException

“Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.” Cela signifie qu'une exception quelconque d'entrée ou sortie (I/O) s'est produite. Cette classe est la classe générale des exceptions pouvant se produire lors de l'échec ou l'interruption d'action d'entrée/sortie. **IOException** est une sous-classe d'**Exception**. Ces exceptions doivent être traitées : à l'aide de blocs **try/catch** ou d'une déclaration.

#### FileNotFoundException

Le constructeur **FileInputStream(String name)** peut lancer une exception de type **FileNotFoundException** si le fichier _name_ n'est pas trouvé.

Revoyez les exercices précédents et ajoutez les blocs try/catch afin de traiter les exceptions déclarées (throws).


## 2. Message secret

Pour cet exercice, vous développer une application qui vous permet d'échanger des messages secrets!

Cette application devra lire le contenu d'un fichier texte, puis le chiffrer (crypter) afin qu’il ne soit plus compréhensible et sauvegarder le résultat dans un deuxième fichier. L’application utilisera une **clé** (un **int**) pour chiffrer le message. La méthode **encrypt** prend trois paramètres soit **inputFile**, **outputFile**, et **key**. Ils sont respectivement le fichier duquel le message sera lu, le fichier où le message secret sera enregistré et la clé qui sera utilisée pour chiffrer le message. La méthode doit lire chaque lettre du fichier d’entrée, chiffrer l'information – pour le cryptage, nous allons ajouter la valeur de la clé à la valeur de la lettre (rappel, la méthode **read()** de **InputStreamReader** retourne une valeur de type **int**) – et écrire le résultat du cryptage dans un fichier de sortie.

La méthode **decrypt** est semblable à la méthode **encrypt**. Par contre, vous devrez vous assurer de faire l’opération inverse par rapport à ce que vous avez fait pendant le cryptage avec la clé qui vous est fournie. Si la clé fournie n’est pas la même que celle qui a été utilisée lors du cryptage, le résultat sera encore incompréhensible.

Voici votre point de départ

* SecretMessage.java

```java
import java.io.*;

public class SecretMessage {

  public static void encrypt(String inputFilem, String outputFile, int key) throws IOException, FileNotFoundException {
    InputStreamReader input = null;
    OutputStreamWriter out = null;
  }

  public static void decrypt(String inputFilem, String outputFile, int key) throws IOException, FileNotFoundException {

  }

  public static void main(String[] args) {

    if (args.length != 4) {
      System.out.println("Usage: java SecretMessage [encrypt|decrypt] inputFile OutputFile key");
      System.exit(0);
    }

    if (args[0].equals("encrypt")) {
      try {
        encrypt(args[1],args[2], Integer.parseInt(args[3]));
      } catch (FileNotFoundException e) {
        System.err.println("File not found: "+e.getMessage());
      } catch (IOException e) {
        System.err.println("Cannot read/write file: "+e.getMessage());
      }
    } else if (args[0].equals("decrypt")) {
      try {
        decrypt(args[1],args[2], Integer.parseInt(args[3]));
      } catch (FileNotFoundException e) {
        System.err.println("File not found: "+e.getMessage());
      } catch (IOException e) {
        System.err.println("Cannot read/write file: "+e.getMessage());
      }
    } else{
      System.out.println("Usage: java SecretMessage [encrypt|decrypt] inputFile OutputFile key");
      System.exit(0);
    }
  }
}
```

Si le fichier **input.txt** contient le message suivant :

```
Here is my sercret message, can you decode it?
```

À la suite de l’exécution de la commande suivante (notez que la clé est 3) :

```bash
>java SecretMessage encrypt input.txt secret.txt 3
```

Le fichier **secret.txt** contiendra :

```
Khuh#lv#p|#vhufuhw#phvvdjh/#fdq#|rx#ghfrgh#lwB
```

Si l’on essaie de décrypter le message en utilisant la mauvaise clé (disons 2)

```bash
>java SecretMessage decrypt secret.txt output.txt 2
```

On obtient encore quelque chose d’incompréhensible dans le fichier output.txt.

```
Ifsf!jt!nz!tfsdsfu!nfttbhf-!dbo!zpv!efdpef!ju@
```

Par contre, si on utilise la bonne clé comme ceci:

```bash
>java SecretMessage decrypt secret.txt output.txt 3
```

On obtient cela dans le fichier output.txt

```
Here is my sercret message, can you decode it?
```

### Défi supplémentaire :

Vous noterez que si l’on utilise une clé qui est grande (quelques dizaines ou plus), l’application peut être défectueuse (ne pas retourner le bon message après le décryptage). Modifiez votre code pour pouvoir utiliser n’importe quelle clé (rappelez-vous des valeurs que la méthode read() peut retourner).


## 3. PlayListManager

Pour ce laboratoire, vous devez modifier l'application PlayListManager afin de lire et écrire les chansons dans des fichiers.

#### 3.1 Lire les chansons à partir d'un fichier

Modifiez l'application afin de lire les chansons à partir d'un fichier. Par exemple, donnez le nom fichier sur la ligne de commande,

```bash
> java Run songs.csv
```

Le fichier contient une entrée par ligne. Chaque entrée est composée du titre, du nom de l'artiste et du titre de l'album. Les champs sont séparés par “ :”.

```
A Dream Within A Dream:Alan Parsons Project:Tales Of Mystery & Imagination
Aerials:System Of A Down:Toxicity
Bullet The Blue Sky:U2:Joshua Tree
Clint Eastwood:Gorillaz:Clint Eastwood
Flood:Jars Of Clay:Jars Of Clay
Goodbye Mr. Ed:Tin Machine:Oy Vey, Baby
Here Comes The Sun:Nina Simone:Anthology
In Repair:Our Lady Peace:Spiritual Machines
In The End:Linkin Park:Hybrid Theory
Is There Anybody Out There?:Pink Floyd:The Wall
Karma Police:Radiohead:OK Computer
Le Deserteur:Vian, Boris:Titres Chansons D’auteurs
Les Bourgeois:Brel, Jacques:Le Plat Pays
Mosh:Eminem:Encore
Mosquito Song:Queens Of The Stone Age:Songs For The Deaf
New Orleans Is Sinking:Tragically Hip, The:Up To Here
Pour un instant:Harmonium:Harmonium
Sweet Dreams:Marilyn Manson:Smells Like Children
Sweet Lullaby:Deep Forest:Essence of the forest
Yellow:Coldplay:Parachutes
```

### 3.2 Écrire les chansons dans un fichier

Modifiez l'application afin de sauvegarder les chansons de la nouvelle liste dans un fichier. Vous pouvez spécifier le nom du fichier sur la ligne de commande,

```bash
> java Run songs.csv workout.csv
```

Le format du fichier est le même que celui en entrée.

Il est fortement suggéré que vous développiez vos solutions des méthodes PlayList getSongsFromFile( String fileName ) et void writeSongsToFile( String fileName ) dans une classe séparée, par exemple Utils. Une fois le travail terminé, intégrez les méthodes à l'application PlayListManager.

* media.zip (point de départ)


## 4. Exceptions

* Ajoutez les déclarations d'exception nécessaires afin de résoudre les erreurs de compilation

```java
import java.io.*;

public class L9 {

  public static String cat(String fileName) {
    FileInputStream fin = new FileInputStream(fileName);
    BufferedReader input = new BufferedReader(new InputStreamReader(fin));
    StringBuffer buffer = new StringBuffer();
    String line = null;

    while ((line = input.readLine()) != null) {
      line = line.replaceAll("\\s+", " ");
      buffer.append(line);
    }

    fin.close();

    return buffer.toString();
  }

  public static void main(String[] args) {
    System.out.println(cat(args[ 0 ]));
  }
}
```

## Ressources

* [https://docs.oracle.com/javase/tutorial/getStarted/application/index.html](https://docs.oracle.com/javase/tutorial/getStarted/application/index.html)
* [https://docs.oracle.com/javase/tutorial/getStarted/cupojava/win32.html](https://docs.oracle.com/javase/tutorial/getStarted/cupojava/win32.html)
* [https://docs.oracle.com/javase/tutorial/getStarted/cupojava/unix.html](https://docs.oracle.com/javase/tutorial/getStarted/cupojava/unix.html)
* [https://docs.oracle.com/javase/tutorial/getStarted/problems/index.html](https://docs.oracle.com/javase/tutorial/getStarted/problems/index.html)
* [https://docs.oracle.com/javase/tutorial/essential/io/](https://docs.oracle.com/javase/tutorial/essential/io/)
* [https://docs.oracle.com/javase/6/docs/api/java/util/Scanner.html](https://docs.oracle.com/javase/6/docs/api/java/util/Scanner.html)
* [https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/Scanner.html](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/Scanner.html)
