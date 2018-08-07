# Json Nodes Sizes Analyser
A web app that analyses and displays the nodes' sizes of a given json


#### Problem :
One of our applications was generating an extremely big Json (70 MB and up) and sending this json to the web UI through the network takes long waiting time.

#### Solution :
The structure of the json can be very complex, in order to optimise the generation of the json and minimise its size we needed to know which objects or arrays are taking most of the size and dislay them in a sorted list.

The solution is a web application that can read a json from an uploaded file, text area or an url and show the results in tables.

#### The application :
The application is a Spring boot application with Thymeleaf interface.
To run the application, enter this command : `mvn clean install spring-boot:run`

and open : [http://localhost:8080/](http://localhost:8080/) 


#### Snapshots from the interface :

##### The input :
![Input part](https://github.com/nabildridi/JsonNodesSizesAnalyser/blob/master/images/input.png)

##### The output :
![Output part](https://github.com/nabildridi/JsonNodesSizesAnalyser/blob/master/images/results.png)
