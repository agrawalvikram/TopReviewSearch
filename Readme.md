# Top Review Search

One Paragraph of project description goes here

## Introduction

This project try to solve the problem for getting top K review from set of [Amazon fine foods reviews] (http://snap.stanford.edu/data/web-FineFoods.html). This application loads the reviews at startup and provides a [Rest API](http://www.restapitutorial.com/) to search top K documents based on given key words which needs to be present in the review.

### Prerequisites

One need to have Java 8 installed on the their system. It can be checked on console by command.

```
java -version
```

### Building

[Gradle](https://gradle.org/) is used as a build tool for the project. 
Jar from the project can be created by running following command from root directory of the project

```
Linux / Mac OS: 	./gradlew clean jar
Windows: 			./gradlew.bat clean jar
```
This will generate jar file at path ./build/libs/document-search.jar

## Running the Application

Generated jar expects two command line parameter as following.

* File location of review and number of reviews to be read from the file. A sample reviews file is kept at [location]. Alternatively this file can be downloaded from this [link]((http://snap.stanford.edu/data/web-FineFoods.html)). 
* Number of reviews to be loaded from the review file

Following command will make the server up.

```
java -jar ./build/libs/document-search.jar ./reviewDocument/finefoods.txt 50000
```

## Exposed APIs

Once server is up and running, following API can be used to fetch the top review data

```
http://localhost:4567/api/search?count=2&query=tasty,veggies,breakfast
```
This will return

```json
[
{
review: {
id: 24577,
productId: "B004VLV8CS",
userId: "A8XEURVHZNINC",
profileName: "Elise Wood",
helpfulness: "0/0",
score: 5,
time: 1337731200,
summary: "just as tasty as oatmeal",
text: "Except I'm getting oats plus a whole bunch of other grains. If you want an oatmealish(but whole grain instead of cut/rolled/chopped) food that's also got lots of other grains in it this might be something to check out. I usually put it in my rice cooker with a cut up apple, cinnamon and a little honey for breakfast. Or with seasonings and chunks of meat/veggies(add veggies at the end to steam) if I want a quick meal."
},
score: 1
},
{
review: {
id: 16142,
productId: "B005EL6VOY",
userId: "A2DBIUHAU45A9V",
profileName: "irunsoicaneat",
helpfulness: "7/7",
score: 5,
time: 1324425600,
summary: "Healthy, tasty, and versatile!",
text: "This is my first review ever on Amazon, but this product deserves attention. I have eaten this product for 5-7 days a week for an entire year now. The only time I didn't eat it was when I was on vacation. Even then, I began to miss it after a week. This product is amazing, and I've yet to tire of it. Don't get me wrong, nothing can replace hashbrowns for breakfast in the morning in terms of flavor, but this product has helped me get into the best shape of my life while still enjoying the food I eat.<br /><br />The versatility of this product is amazing. If I want a porridge consistency, I add slightly more water. If I want a bowl of something with more chew to it I add slightly less water. If I'm extra hungry , I'll cook it on the stovetop with frozen veggies (carrots, peas, corn, broccoli, etc) with some garlic salt and hot sauce and you've got a savory and healthy dinner. If I'm in the mood for something sweet, I'll add bananas and drizzle in some honey. A bowl of this with an over-easy egg and you've got a very filling snack under 250 calories. With a regular meal, I'll sub out my starch (rice, pasta, bread) with this oatmeal for a healthy alternative. For me, I feel more full with oatmeal as the side dish probably because it fools my stomach with extra water weight but the fiber content keeps me feeling full or at least content until the next meal.<br /><br />Think outside the box and be open to new flavors and new textures, and you will thoroughly enjoy this product as much as I have, AND feel healthy doing so :)"
},
score: 1
}
]
```

## Built With

* [Spark](http://sparkjava.com/) - A micro framework for web application
* [Gradle](https://gradle.org/) - Java project build tool

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests.


## Authors

* **Jitendra Kushwaha** - *jitendra.theta@gmail.com* 


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Thanks to Stackoverflow :)
