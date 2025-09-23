# Weather Display

## Overview

In this exercise, you will create an app that shows weather information. The program will use real weather data from [OpenWeather](https://openweathermap.org). We have created code to handle getting the data from OpenWeather, and have given you a start on the user interface (UI). You will complete the UI.

When you are done, your results might look something like this, with the bonus option to invent your own weather widgets:

![Completed user interface with 4 widgets](doc/images/completed-ui.png)

As always, make sure to follow the [Comp 127 Java Style Guide](https://f25.comp127.innig.net/resources/style-guide).

### Learning Goals

- Practice writing code that works with real, live data
    - Formatting data as text
    - Working with null values
- Practice implementing a user interface
    - Doing coordinate math
    - Using groups of elements
- Practice working with inheritance
    - Overriding methods
    - Using methods inherited from a superclass

### Attributions

We have used the owmjapis and org.json packages (both of which are open source) to make it easier for you to use OpenWeather data. You do not need to know how these packages work, but if you would like more information about them, you can check out the following links:
- [owmjapis](https://bitbucket.org/akapribot/owm-japis)
- [json](https://github.com/stleary/JSON-java)

The original version of this activity was by Bret Jackson, with heavy contributions from Daniel Kluver and Paul Cantrell.


## Part 0: Get an API Key

**Do this one part early, as soon as this exercise is assigned.**

You will first need to get your own OpenWeather API key, so you can use their web API to get weather data. Do this by signing up for a free account at [https://openweathermap.org/appid](https://openweathermap.org/appid).

> **What is a web API?** In general, an “application programming interface,” or “API” for short, is a set of rules and promises that one piece of code makes about what it will do, and how other pieces of code can communicate with it. You are already used to using _library APIs_, such as `List` and `CanvasWindow`, that let code communicate with other code in the _same_ process or application, and on the _same_ computer.
>
> However, code can also communicate with other code on other computers, and even across the Internet. We call that a “remote API,” or if it specifically uses web technology such as HTTP, a “web API.” (In less-technical business circles, when people say “API,” they usually mean “web API.”)

> **What is an API key?** OpenWeather provides this API for free, so that people can build apps and do research. The API key lets them know who is requesting data. This lets them have some paid features, and lets them shut down a particular account if it is abusing the system (e.g. by making too many requests).

Once you've completed the sign-up process and **confirmed your email address**, there are two places you can find your API key:

- You should receive an email from OpenWeather with your API key after confirming your email, or…
- you can find it in [My API Keys](https://home.openweathermap.org/api_keys) on the OpenWeather site.

Your API key will be a random sequence of 32 numbers and letters.

Inside `res/`, create a new file named `weather-display.properties`. ⚠️ Make **sure** you have the correct location! ⚠️ In VS Code’s UI, it is easy to accidentally create the file inside of `res/condition-icons/`. Make sure you create the file just inside of `res/`.

Put this one line in the newly created file:

    api.key=???????

…where `???????` is your API key.

Run `WeatherProgram` to test your API key. NOTE: If you get an error message that your api key is invalid, add a newline character at the end of your api key. If it works, you will see a weather display that is not very pretty, but has the real-life current weather conditions for Macalester:

![Initial UI state](doc/images/initial-ui-state.png)


## Part 1: Understand the existing code

### Data model

In the `comp127.weather.api` package, look at these classes:

- `WeatherData`
- `Conditions`
- `CurrentConditions`
- `ForecastConditions`

Study the relationships between these classes. (How are they connected? What are the has-a and is-a relationships between them? What is the role of each class? It might be wise to diagram this.)

These classes have some tricky code in them, including some Java features we have not studied in class. But don’t worry! You only need to **use these classes, not modify them** or understand how they work. That means that you can just pay attention to the **public method signatures** and their accompanying Javadoc. For example, in `WeatherData`, the signature of the first public member is:

```java
public String getCityName()
```

These public methods are what you will use to get data for your weather UI. Note that these classes provide _just the data_; none of them say anything about _how the data will be displayed_. These “no view, just data” classes are called a **data model**. You will need to be familiar with the data model to build your UI.

### User interface

Look inside the `comp127.weather.widgets` package. There is an interface called `WeatherWidget`, plus a start on two specific widgets that implement it. You will complete those two widgets, and optionally add some of your own. Study `WeatherWidget` and understand the methods that each widget must implement.

There are also a few classes in that package that will help you implement widgets. Take a look at them, and understand what they offer you.

In the `comp127.weather` package, take a look at `WeatherProgram`. This is the full UI that is going to display your widgets. You can run it now if you want! It will fetch real weather data for St. Paul. However, it currently just shows the same widget several times, and does not yet show an enlarged version of the selected widget as it is supposed to.

If you see nothing but question marks, that means either you have not put your API key in the correct place, or your key is not active yet. Even if your API key is not active yet, however, you can still proceed with the assignment, because of the…

### Testing strategy

In the `test` directory, in the `comp127.weather.widgets` package, run `SingleWidgetTest`. This class is designed to help you test new widgets as you develop them. Right now, it is set up to test `TemperatureWidget`, but you will eventually use it to test others too.

Much like `WeatherProgram`, this class displays widgets on a canvas. However, there are two important differences:

- **It shows just one widget at many different sizes.** This is to make sure the widget scales itself properly, so that it looks good whether it is large or small.
- **It uses fake data generated for testing.** Why use fake data?
    - The fake data is designed to exercise a wide range of possible conditions, including **edge cases** such as missing data and extreme values.
    - The fake data **does not use the network**, so your tests run faster (and don’t unnecessarily pummel the OpenWeather servers).
    - The fake data it uses is **stable**, i.e. the same every time. This means that if you catch a bug, you can retest your widget with the same data to make sure you fixed it. A stable, repeatable test environment like this is called a **test fixture**. (You can see how the fake data is generated in `WeatherDataFixtures`. In this project it is arbitrarily generated, but fixture data is often hand-designed or captured from real data.)

When `SingleWidgetTest` runs, you should see a blank screen. This is how the widget will look before it gets any data from OpenWeather, i.e. while it is loading.

Click anywhere. You will now see how the widget looks when the server responds with everything blank that is allowed to be blank.

Click again. You will see a variety of different weather conditions.

Do you notice that `TemperatureWidget` doesn’t look quite right?


## Part 2: Fix `TemperatureWidget`

We want `TemperatureWidget` to look something like this:

![Temperature widget working in various states](doc/images/temperature-widget-completed.png)

…but right now, it looks like this:

![Temperature widget showing bugs in various states](doc/images/temperature-widget-starting-point.png)

There are several problems:

- It does not handle null temperatures well. (User interfaces shouldn’t show the word “null.”)
- It shows the temperature to a ridiculous number of decimal places.
- The textual description of the weather condition is stuck almost entirely outside the box in the upper left.

Your first task: **fix these problems** to make `TemperatureWidget` work properly.

Here are some hints:

- Look for the `TODO` comments in the code.
- Note that many of the numbers in the data model have the type `Double` with a capital D, which (unlike `double`) allows the value `null` (meaning “no data available”).
- There are some helpful tools in `FormattingHelpers`. You can use those formatter objects by calling their `format` methods, like this: `FormattingHelpers.ONE_DECIMAL_PLACE.format(someNumber)`. However, those formatters will not handle `null` values correctly. Before formatting the temperature, you will need to check for null using a conditional.
- You will need to do null checks a lot, so consider adding a new static method to `FormattingHelpers` that first checks for null, then uses `ONE_DECIMAL_PLACE` if a value is present, and return `"-"` if the value is null. (You could even make `ONE_DECIMAL_PLACE` private so you don’t accidentally use it from your widgets, and have to use your null-safe helper instead.)

Use `SingleWidgetTest` to make sure the widget works with a wide variety of input data.


## Part 3: Complete `ForecastWidget`

Make the `ForecastWidget` look like this:

![Forecast widget working](doc/images/forecast-widget-completed.png)

The boxes at the bottom represent a timeline of future forecasts. When the mouse moves over the boxes, it should change the forecast shown above.

The code contains some scaffolding and many hints. Think, plan, sketch on paper, and take it one step at a time.

Change `SingleWidgetTest` to test `ForecastWidget`. Test it thoroughly! There is a lot going on here, which means there are many possible bugs.


## Part 4: Complete `WeatherProgram`

You’ve been using the test code so long, it may be easy to forget the goal is to build a real app!

Go back to `WeatherProgram`, and find the `createWidgets()` method. This method gets called twice: once to make small widgets to run down the right hand side of the screen, and once to make large widgets to show one at a time on the left.

The layout logic for these widgets is all done, but you never see the enlarged widgets because `selectWidgetAtIndex()` is not implemented yet. Implement it according to the hints in the code.


## Bonus: Create your own widget(s)

If you want more practice working with graphics and user interfaces, or are just feeling especially creative:

Using the general structure of `TemperatureWidget` and `ForecastWidget` as a guide, invent your own weather widget. You might use the widgets in the screenshot at the very top of this document for inspiration. You might also be inspired looking through what is available to you in `WeatherData` and its related classes.

To test your new widget, change `SingleWidgetTest` to run it instead of the temperature widget.

To learn about what kinds of graphics you can create, take a look at the [comp127graphics javadoc](https://mac-comp127.github.io/kilt-graphics/).

Add your new widget to `WeatherProgram` to see it in action!
