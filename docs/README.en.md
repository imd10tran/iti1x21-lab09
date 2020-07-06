# ITI1121 - Lab 09 - Java I/O, Secret Messages and Exceptions

## Submission

Please read the [junit instructions](JUNIT.en.md) for help
running the tests for this lab.

Please read the [Submission Guidelines](SUBMISSION.en.md) carefully.
Errors in submitting will affect your grades.

Submit your answers to

* SecretMessage.java
* PlayList.java
* Song.java
* SortByAlbum.java
* SortByArtist.java
* SortByName.java

## Objectives

* **Design** a Java program to read data from the keyboard or from a text file.
* **Design** a Java program to write data to the output or to a text file.
* **Modify** an existing Java program to declare all reportable exceptions.

This laboratory has multiple parts.

## 1. Java I/O

The first part presents the basic concepts related to Java I/O that you will need for your work (save these lecture notes and examples as they will be useful for your data structure course next year!). The second part requires developing an application that allows you to communicate via secret messages. The third part requires modifying an application called the PlayListManager for reading and writing songs from/to a file.

This document introduces the basic elements of the input/output (I/O) system in Java. In particular, it covers a subset of the classes that are found in the java.io package. Since version Java 1.4, a new package has been introduced, called java.nio (new io), that defines more advanced concepts, such as buffers, channels and memory mapping. However these topics are not covered here.

Java I/O seems a bit complex at first. There are many classes. Also, objects from two or more classes need to be combined for solving specific tasks. Why is that? Java is a modern language that has been developed when the World Wide Web was becoming a reality. As such, the data can be written/read, to/from many sources, including the console/keyboard, external devices (such as hard disks) or the network. The presence of the Web stimulated the creation of classes for handling various encodings of the information (English and European languages but also Arabic and Asian languages; and also binary data).

### 1.1 Definitions

A **stream** is an ordered sequence of data that has a source or a destination. There are two major kinds of streams: **character** streams and **byte** streams.

In Java, characters are encoded with Unicodes — character streams are associated with text-based (human readable) I/O. The character streams are called readers and writers. This document focuses mainly on character streams. Byte streams are associated with data-based (binary) I/O. Examples of binary data include image and audio files, jpeg and mp3 files. Information can be read from an external source or written to an external source. Therefore, for (nearly) every input stream (or reader) there is a corresponding output stream (or writer). Besides input and output, there is a third access mode that is called direct access, which allows to read/write the data in any given order. The direct access mode is not covered here.

### 1.2 Overview

Several classes are found in the I/O package, reflecting the fact that I/O involves two different kinds of streams and three different access modes.

* **Stream**: character, byte;
* **Access**: read, write, direct.

Furthermore, the medium that is used (keyboard, console, disk, memory or network) dictates its own constraints (the requirement for a buffer or not, for instance). The I/O package contains some 50 classes, 10 interfaces and 15+ Exceptions. The large number of classes involved is enough to confuse a beginner programmer. On the top of this, in general, objects from 2 or 3 classes need to be combined to carry out a basic task. For example:

```java
InputStreamReader in = new InputStreamReader(new FileInputStream("data"));
```

where “data” is the name of the input file. The following sections are presenting the main concepts related to Java I/O. Most concepts are accompanied by simple examples and exercises, which illustrate specific topics. Compile and run all the examples. Complete all the exercises.

### 1.3 Streams

**InputStream** and **OutputStream** are two abstract classes that define the methods that are common to all input and output streams.

#### 1.3.1 InputStream

The class InputStream declares the following three methods.

* **int read() :** Reads the next byte of data from the input stream. The value of the byte is returned as an int in the range 0 to 255\. If no byte is available, because the end of the stream has been reached, the value -1 is returned;
* **int read(byte[] b) :** Reads some number of bytes from the input stream and stores them into the buffer array b. The number of bytes actually read is returned as an integer;
* **close() :** Closes this input stream and releases any system resources associated with the stream.

Since **InputStream** is an abstract class it is never instantiated. Here are examples of its subclasses: **AudioInputStream, ByteArrayInputStream, FileInputStream, FilterInputStream, ObjectInputStream, PipedInputStream, SequenceInputStream, StringBufferInputStream**. One of the main InputStream classes that will be of interest to us is the **FileInputStream**, which obtains input bytes from a file in a file system. More on that later.

#### 1.3.2 OutputStream

The class OutputStream declares the following three methods.

* **write(byte[] b) :** Writes b.length bytes from the specified byte array to this output stream;
* **flush() :** Flushes this output stream and forces any buffered output bytes to be written out;
* **close() :** Closes this output stream and releases any system resources associated with this stream.

Since **OutputStream** is an abstract class, subclasses are used to create objects associated with specific types of I/O: **ByteArrayOutputStream, FileOutputStream, FilterOutputStream, ObjectOutputStream** and **PipedOutputStream**. **FileOutputStream** is commonly used. A file output stream is an output stream for writing data to a file.

#### 1.3.3 System.in and System.out

Two objects are predefined and readily available for your programs. **System.in** is an input stream associated with the keyboard, and **System.out** is an output stream associated with the console.

### 1.4 Steps

Writing to (or reading from) a file generally involves three steps:

* Opening the file
* Writing to (or reading from) the file
* Closing the file

Closing the file is important to ensure that the data are written to the file, as well as to free the associated internal and external resources.

### 1.5 Reading in

Let’s narrow down the discussion to reading from a file or reading from the keyboard.

#### 1.5.1 Reading from a file

Reading from a file involves creating a FileInputStream object. We’ll consider two constructors.

* **FileInputStream(String name) :** This constructor receives the name of the file as an argument. Example:

```java
InputStream in = new FileInputStream("data");
```
* **FileInputStream(File file) :** This constructor receives as an argument a **File** object, which is the representation of an external file.

```java
File f = new File("data");
InputStream in = new FileInputStream(f);
```

Having a **File** object allows for all sorts of operations, such as:

```java
f.delete();
f.exists();
f.getName();
f.getPath();
f.length();
```

**FileInputStream** is a direct subclass of **InputStream**. The methods that it provides allows reading bytes.

#### 1.5.2 InputStreamReader

Because a FileInputStream allows reading bytes only, Java defines an InputStreamReader as bridge from byte to character streams. Its usage is as follows.

```java
InputStreamReader in = new InputStreamReader(new FileInputStream("data"));
```

or

```java
InputStreamReader in = new InputStreamReader(System.in);
```

where **System.in** is generally associated with the keyboard of the terminal.

* **int read() :** Reads a single character. Returns -1 if the end of stream has been reached. The return type is int, the value -1 indicates the end-of-file (eof) (or end-of-stream (eos)). The value must be interpreted as a character, i.e. must be converted,

```java
int i = in.read();
if (i != -1) {
  char c = (char) i;
}
```

See

* Unicode.java
* Keyboard.java

* **Reads** characters into an array. Returns the number of characters read, or -1 if the end of the stream has been reached. Examples:

```java
char[] buffer = new char[ 256 ];
num = in.read(buffer);
String str = new String(buffer);
```

## Exercise 1 :

Write a class that reads characters for the keyboard using the method **read(char[] b);** the maximum number of characters that can be read at a time is fixed and determined by the size of the buffer. Use the class Keyboard from the above example as a starting point. Here is a list of modifications that you have to do:

1.  You no longer need the variable "i" to stock the values that were read. You will use a _buffer_ of type **char[]** instead, as shown previously.
2.  You will need to change the condition statement for the while loop. Notably, the variable "i" is not part of the solution anymore, you will need to use the method **read(char[] b)** instead.
3.  Once the data is read by the method read, it is stored in the buffer. You will need to convert this buffer to a String.
4.  Note that some symbols are not displayable. You will need to use the method trim to remove these characters. For example, **yourString.trim()** returns a new object of type String without the characters that are not displayable.
5.  Write the characters that you got to the console.
6.  Finally, empty your buffer using the following command: **Arrays.fill(buffer, '\u0000');**

Run some tests, do you notice anything bizarre? No matter how many characters you’ve entered the resulting String is always 256 characters long.

Here is the initial version of

* Keyboard.java

#### 1.5.3 BufferedReader

For some applications, the input should be read one line at a time. For this, you will be using an object of the class **BufferedReader**. A BufferedReader uses an InputStreamReader to read the content. The **InputStreamReader** uses an **InputStream** to read the raw data. Each layer (object) adds new functionalities. The InputStream reads the data (bytes). The InputStreamReader converts the bytes to characters. Finally, the BufferedReader regroups the characters into groups, in this case into **lines**.

```java
FileInputStream f = FileInputStream("data");
InputStreamReader is = new InputStreamReader(f);
BufferedReader in = new BufferedReader(is);
```

or

```java
BufferedReader in = new BufferedReader(
new InputStreamReader(
new FileInputStream("data")));
String s = in.readLine();
```

The class **Copy** is a program that copies the content of a file to the console.

Note that we do not handle exceptions in this example.

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

You can see that the program **Copy** copies the content of the file given as a parameter one character at a time, until the end of the data stream, -1, is read by the **InputStreamReader**. The program also includes the three steps shown before: opening a file, reading the file, and closing the file.

* Copy.java

## Exercise 2 :

Write a class that prints all the lines that contain a certain word. For each line containing the word, print the line number followed by the line itself.

* You will need to use the method **readLine()** from the class **BufferedReader**. This method returns a String containing the content of the line or null if you have reached the end of the file.
* You will then need to check if the word is part of the String. To do so, use the method **indexOf(yourWord)** that returns the position of your word in the String or -1 if it is not part of the String.

Here is the code that you will need to modify for this exercice:

* Find.java

## Exercise 3 :

Write a class that counts the number of occurrences of the word **Ottawa** in the following file:

* Ottawa.txt

**Hint**: once you have found the first index of Ottawa in a string, you can divide that string using the String method **substring (int index)** that returns a new object of type String containing a subset of the original String. This subset starts with the character at the specified index and extends to the end of the original String.

Instead of a file, it is possible to use a **URL** as a data source. In that case, you will need to import “**java.net.\***” and create an object URL. When we create the **InputStreamReader**, we need to make sure that we call the method **openStream** for the object URL for the information to be read.

```java
URL address = new URL("http://www.google.ca");
InputStreamReader is = new InputStreamReader(address.openStream());
```

## Exercise 4 :

Write a class that fetches the content of a Web page and prints it on the console. Try it with different web address; try it with the web page of this laboratory.

### 1.6 Writing out

Let’s narrow down the discussion to writing to the console and writing to a file. Notice the similarities with reading in; how the process is mirrored.

#### 1.6.1 Writing to a file

Writing to a file involves creating a **FileOutputStream** object. We’ll consider two constructors.

* **FileOutputStream(String name) :** Creates an output file stream to write to the file with the specified **name**. Example:

```java
OutputStream out = new FileOutputStream("data");
```

* **FileOutputStream(File file) :** This constructor receives as an argument a File object, which is the representation of an external file.

```java
File f = new File("data");
OutputStream out = new FileOuputStream(f);
```

* **FileOutputStream** is a direct subclass of OutputStream. The methods that it provides are for writing bytes.

* **OutputStreamWriter :** Because FileOutputStream writes bytes only. Java defines an OutputStreamWriter as a bridge from character streams to byte streams. Its usage is as follows.

```java
OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("data "));
```

or

```java
OutputStreamWriter out = new OutputStreamWriter(System.out);
OutputStreamWriter err = new OutputStreamWriter(System.err);
```

**System.err** is the standard destination for error messages. The methods of an **OutputStreamWriter** are:

* **write(int c) :** Writes a single character.
* **write(char[] buffer) :** Writes an array of characters.
* **write(String s) :** Writes a String.

## Exercise 5

Modify the class Copy.java so that it has a second argument that will be the name of a destination file. Accordingly, write a copy of the input to the output file.

* **PrintWriter :** Prints formatted representations of objects to a text-output stream. It implements the following methods.

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

Similarly, the following methods also terminate the current line by writing the line separator string (which varies from one operating system to the next).

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

### 1.7 CSV files

A Comma-Separated Values (CSV) file is a plain-text file, in which the data is stored in column by column, and is split by a separator. The separator is normally a comma “,”. Basically, it is a file format which allows data to be saved in a table structured format, which can be very useful. Files in the CSV format can be imported to and exported from programs that store data in tables. A CSV parser can then take a CSV file and convert the CSV text input into arrays or objects, to be used effectively in a program.

For example, here is a simple CSV file containing phone book country area codes:

```csv
"1","US","United States"
"2","MY","Malaysia"
"3","AU","Australia"
```

In a simple application, we use the standard **split()** function to parse the CSV file. The following is a simple CSV file, saved under an example directory "/Users/admin/csv/country.csv":

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

To parse the CSV file, just read the above file, and split it by the separator, in this case the comma "," (**line.split(",")**). You can then take the parsed text and use it or format it however you'd like.

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

Running the above code produces one possible way of formatting the data into the following output:

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

When parsing a CSV file, normally there are two issues:

1.  The separator is also contained in the field. For example: "aaa","b,bb","ccc". In this case comma "," is used as a separator, but it also appears in the field **b,bb** which you would not want to separate.
2.  The double-quotes are used to enclose fields, and the field contains double-quotes. For example: "aaa","b""bb","ccc". In this case the double-quote is contained in the field **b""bb**. Note for the dubble-quote to appear inside the field it must be escaped by preceding it with another double-quote.

With these two issues, there are more advanced solutions needed to parse a formatted CSV file with fields containing separator or double-quotes.

### Review: exceptions

This section revisits certain key concepts linked to the handling of Java exception and presents them related to Java I/O

#### IOException

This exception signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations. This signifies that some sort of I/O exception occurred. This is a general exception class that can occur if there is a failure or interruption of an I/O operation. **IOException** is a subclass of **Exception**. This exception has to be handled using a try/catch block or a declaration.

#### FileNotFoundException

The constructor **FileInputStream(String name)** can throw an exception of type **FileNotFoundException** if the file _name_ is not found.

Review the previous exercises and add try/catch blocks to handle the declared exceptions (throws).


## 2. Secret messages

For this exercise, you will need to develop an application that allows you to communicate via secret messages!

This application will need to read text from a file, encrypt this text so that it is no longer readable, and write that new text in a second file. The application will use a **key** (int) to encrypt the message. The method **encrypt()** takes three parameters: **inputFile**, **outputFile** and **key**. The method has to read each letter from the input file, encrypt that letter –for the encryption we will add the value of the key to the value of the letter (recal: **InputStreamReader** has a method **read()** that returns an **int**)—and write that result in an output file.

The method **decrypt()** is similar to the method encrypt. The difference is that this one has to reverse the encryption by doing the opposite operations using the same key. If the key provided for the decryption is not the same as the key for encryption, the result will still be unreadable.

Here is the base code for the class

* **SecretMessage.java** :

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

If I have the following message in the file **input.txt**:

```
Here is my sercret message, can you decode it?
```

After the execution of this command (note that the key is 3):

```bash
>java SecretMessage encrypt input.txt secret.txt 3
```

The file **secret.txt** will have:

```
Khuh#lv#p|#vhufuhw#phvvdjh/#fdq#|rx#ghfrgh#lwB
```

If we try to decrypt the message using the wrong key (let’s say 2)

```bash
>java SecretMessage decrypt secret.txt output.txt 2
```

We still get something unreadable in the file output.txt

```
Ifsf!jt!nz!tfsdsfu!nfttbhf-!dbo!zpv!efdpef!ju@
```

But if we use the right key like this:

```bash
>java SecretMessage decrypt secret.txt output.txt 3
```

We get the following in the file output.txt

```
Here is my sercret message, can you decode it?
```

### Extra challenge:

You might realize that if we use a high number as a key, the application can stop working properly (not returning the proper message after the decryption). Modify your code to be able to take any number as a key. (**hint**: try to remember what values the method read() can return)


## 3. PlayListManager

For this laboratory, you will modify the program PlayListManager to read and write Songs from and to a file.

### 3.1 Reading Songs from a file

Modify the PlayListManager to read Songs from a file. For example, the name of the input file can be specified on the command line,

```bash
> java Run songs.csv
```

The input file contains one entry per line. Each entry consists of the title of the Song, the name of the Artist and the title of the Album. The fields are separated by “:”.

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

### 3.2 Writing Songs to a file

Modify the PlayListManager to write the Songs from the new PlayList to a file. For example, the name of the output file can be specified on the command line,

```bash
> java Run songs.csv workout.csv
```

The output file contains one entry per line. Each entry consists of the title of the Song, the name of the Artist and the title of the Album. The fields are separated by “:” (same as the input).

**Suggestion:** develop and test the methods **PlayList getSongsFromFile(String fileName)** and **void writeSongsToFile(String fileName)** in a separate class, say Utils. Once the methods are working add them to the **PlayListManager** application.

* media.zip (starting point)


# Fourth part

## Exceptions

* Add all the necessary exception declarations to remove the compile-time errors

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

## Resources

* [https://docs.oracle.com/javase/tutorial/getStarted/application/index.html](https://docs.oracle.com/javase/tutorial/getStarted/application/index.html)
* [https://docs.oracle.com/javase/tutorial/getStarted/cupojava/win32.html](https://docs.oracle.com/javase/tutorial/getStarted/cupojava/win32.html)
* [https://docs.oracle.com/javase/tutorial/getStarted/cupojava/unix.html](https://docs.oracle.com/javase/tutorial/getStarted/cupojava/unix.html)
* [https://docs.oracle.com/javase/tutorial/getStarted/problems/index.html](https://docs.oracle.com/javase/tutorial/getStarted/problems/index.html)
* [https://docs.oracle.com/javase/tutorial/essential/io/](https://docs.oracle.com/javase/tutorial/essential/io/)
* [https://docs.oracle.com/javase/6/docs/api/java/util/Scanner.html](https://docs.oracle.com/javase/6/docs/api/java/util/Scanner.html)
* [https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/Scanner.html](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/Scanner.html)
