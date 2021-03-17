# Merged photo albums challenge

A client merged their home photo folder with their partner's folder, with disastrous consequences.
They now have a lot of duplicate photos in different places in the folder structure.

Write a command-line program which finds files which have exactly the same contents and outputs any duplicates (and their locations) to standard output.

### Requirements
- Only use the standard libraries of your chosen language. No shelling out, or using external services or libraries.
- External libraries can be used for the purposes of testing.
- The code should be readable, reusable, easy to modify and well tested.

### Consider the following points in formulating your solution
- What if this same solution was used on a really large set of photos? What if it was a thousand photos? Or tens of thousands? [COVERED]
- What if this was a three-way merge, with triplicates? Does your solution account for this? [COVERED]
- Some of these files may have had their filename changed. [COVERED]
- Some of these may have only their extension changed. [COVERED]


## Prerequisites
- Java 15
- Apache Maven

## Instruction to run in Command Line
- Go to `./DuplicateFile` directory
- Run `javac ./src/main/ava/com/duplicatefile/*.java`
- Run `java -cp src/main/java/ com.duplicatefile.DuplicateFileSearch` search for duplicate files in Cogent provided data set
- Run `java -cp src/main/java/ com.duplicatefile.DuplicateFileSearch "./locationWhereToLookForDuplicateFiles"` search for duplicate files in specified location. Only the first argument will be used.

## To run Tests
- Go to `./DuplicateFile` directory
- Run `mvn test` in command line

## Implementation and design decisions
1. Application will get paths of all regular files with opaque content under provided location.
2. To satisfy a requirement to find files which have exactly the same contents I had to get each file's content in byte[] and check if they match.
   I chose to use Map<Integer, List<Path>> data structure for this task. <br>
   Where Key is hash code of file's content in byte[] and Value is list of paths that matches same key, meaning that files with exactly same contents will have same Key but different paths. <br>
   I understand that my Key will be hashed again, but it's a small price to pay comparing to saved memory if file's content in byte[] would be stored instead. <br>
   Integer object in Java occupies 16 bytes, what is way less than smallest image in provided data set ~7056 bytes. <br>
   Note: I used `Files.readAllBytes(Path)` method to get file's content in byte[] which won't work with files larger than ~2.147 GB.
3. Program will iterate through above mention map and for every Key, Value pare with more than one Path, <br>
   will create DuplicateFile object, which will be added to the list for output. <br>
   Alternatively I could use StringBuilder to output results, however, I chose to keep them in DuplicateFile <br>
   in case some other actions might be need to be done for identified duplicates. <br>
   Note: I wasn't sure how exactly standard output should look, but my implementation <br>
   `[File [ photo1.jpg ] has 1 duplicate: [C:\Users\tmp\photo3.jpg]]` can be easily changed.

## Not covered scenario
I have found an interesting scenario, where images `./Dec 2016/scary bear.JPG` and `staring contest.jpg` look the same but with different dimensions (and maybe some other properties). <br>
I thought I could cover this case if I'll resize images to the same dimensions and then do comparison, however, images still had different byte[] content. <br>
After some research I found a way or two how this case might be covered, but it would require more time than I expect this challenge is build for. Please let me know if I'm wrong :)

## Author
Andrius Marciulionis    
anmarciulionis@gmail.com

Code written and tested on Windows 10 OS