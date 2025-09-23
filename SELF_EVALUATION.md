# Take-Home Activity: Weather Display

## Self-Evaluation Rubric

# 🔴 Instructions

You can make a copy of this document and use the checkboxes below as you complete the self-evaluation of your solution.

# Correctness

Use the list below to evaluate the correctness of your solution. Cross off the items that your solution passes.

- [ ] The `WeatherProgram` class should have these components:
  - [ ] `createWidgets()`: works so that at least 2 widgets are shown (or more if you did the bonus)
  - [ ] `selectWidgetAtIndex()` is implemented so that it:
    - [ ] returns without doing anything if index is out of bounds, per hint in the code
    - [ ] removes the old large widget, if it exists, before replacing it
    - [ ] updates `displayedLargeWidget` to the correct new widget
    - [ ] adds the newly selected large widget to the canvas
- [ ] The `TemperatureWidget` class should have these components:
  - [ ] The user interface does not print “null” when data has a null temperature
  - [ ] The temperature is rounded to (at most) two decimal places for readability
  - [ ] The textual description of the weather condition is placed in a readable location
  - [ ] There widget shows “°F” or some other temperature units label
- [ ] The `ForecastWidget` class:
  - [ ] The ForecastWidget shows rows of `ForecastBoxes`
  - [ ] If there are more boxes than will fit in one row, the boxes wrap to the next row
  - [ ] When the mouse moves over the boxes it should change the forecast
  - [ ] When forecast data updates, the number of boxes increases and decreases as appropriate (Test this by clicking through the test cases in `SingleWidgetTest`)
- [ ] Your widgets work with live data
- [ ] (Optional) Create your own widget(s)

# Code Style

Use the list below to evaluate the adherence of your code to the [COMP127 Style Guide](https://f25.comp127.innig.net/resources/style-guide/). Cross off the items that your code satisfies.

- [ ] all classes are in packages
- [ ] package names start with a lowercase letter
- [ ] newly created Java files have a header comment with (a) author name, (b) brief description of class, and (c) acknowledgement, if appropriate
- [ ] identifier (variable and method) names are descriptive
- [ ] **variable** and **method** names are in `lowerCamelCase` (no `CapitalizedNames`, `names\_with\_underscores`)
- [ ] **class** names are singular nouns
- [ ] **class** names are in `UpperCamelCase`
- [ ] all instance variables are **private**
- [ ] proper indentation:
  - [ ] opening curly braces (“{”) are at the end of the line
  - [ ] closing curly braces (“}”) are on their own line
  - [ ] one space before each opening curly brace
  - [ ] the indentation of closing braces is the same as the indentation of the opening statements they match
  - [ ] lines are indented according to how deeply they are nested
- [ ] completed TODO comments are deleted
- [ ] extra blank lines are deleted
- [ ] unneeded commented lines of code are deleted
- [ ] print statements used for testing are deleted

# Reflection

Briefly reflect, in writing, on your experience solving this exercise. What did you miss?  What did you wish you did better?  What challenges did you face, and how did you overcome them?
