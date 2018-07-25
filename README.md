# jwht-htmltopojo

## Introduction : Java HTML to POJO framework

This lib provides a lightweight, fully featured, highly pluggable 
and customizable Java Html to Pojo converter framework.

It was built to be used with [jwht-scrapper](https://github.com/whimtrip/jwht-scrapper)
which provides a gateway to create fully customisable real world HTTP
scrappers with all the features required by classical scrapping 
usecases.

jwht-htmltopojo features a complete javadoc that can be seen from 
github/official code sources to fully interact with all the 
possibilities offered by this library. Heavily interfaced, most 
processing units can be replaced or extended to fit your use case
if required. For easier processings, creating custom processing
unit can be done via custom conversion classes linked through
annotation on your POJOs.

This lib uses under the hood [jsoup](https://jsoup.org/) and was
highly inspired by [jspoon](https://github.com/DroidsOnRoids/jspoon).


## Installation

You can install this artifact from maven central repository.

### Maven
```
    <dependency>
             <groupId>fr.whimtrip</groupId>
             <artifactId>whimtrip-ext-htmltopojo</artifactId>
             <version>1.0.0</version>
    </dependency>
 ```

## How to use : Basics


Imagine we need to parse the following html page :

```
<html>
    <head>
        <title>A Simple HTML Document</title>
    </head>
    <body>
        <div class="restaurant">
            <h1>A la bonne Franquette</h1>
            <p>French cuisine restaurant for gourmet of fellow french people</p>
            <div class="location">
                <p>in <span>London</span></p>
            </div>
            <p>Restaurant n*18,190. Ranked 113 out of 1,550 restaurants</p>  
            <div class="meals">
                <div class="meal">
                    <p>Veal Cutlet</p>
                    <p rating-color="green">4.5/5 stars</p>
                    <p>Chef Mr. Frenchie</p>
                </div>
                
                <div class="meal">
                    <p>Ratatouille</p>
                    <p rating-color="orange">3.6/5 stars</p>
                    <p>Chef Mr. Frenchie and Mme. French-Cuisine</p>
                </div>
                
            </div> 
        </div>    
    </body>
</html>

```

Let's create the POJOs we want to map it to :

```
public class Restaurant {

    @Selector( value = "div.restaurant > h1")
    private String name;
    
    @Selector( value = "div.restaurant > p:nth-child(2)")
    private String description;
        
    @Selector( value = "div.restaurant > div:nth-child(3) > p > span")    
    private String location;    
    
    @Selector( 
        value = "div.restaurant > p:nth-child(4)"
        format = "^Restaurant n\*([0-9,]+). Ranked ([0-9,]+) out of ([0-9,]+) restaurants$",
        indexForRegexPattern = 1,
        useDeserializer = true,
        deserializer = ReplacerDeserializer.class,
        preConvert = true,
        postConvert = false
    )
    // so that the number becomes a valid number as they are shown in this format : 18,190
    @ReplaceWith(value = ",", with = "")
    private Long id;
    
    @Selector( 
        value = "div.restaurant > p:nth-child(4)"
        format = "^Restaurant n\*([0-9,]+). Ranked ([0-9,]+) out of ([0-9,]+) restaurants$",
        // This time, we want the second regex group and not the first one anymore
        indexForRegexPattern = 2,
        useDeserializer = true,
        deserializer = ReplacerDeserializer.class,
        preConvert = true,
        postConvert = false
    )
    // so that the number becomes a valid number as they are shown in this format : 18,190
    @ReplaceWith(value = ",", with = "")
    private Integer rank;
        
    @Selector(value = ".meal")    
    private List<Meal> meals;
    
    // getters and setters
        
}

```

And now the `Meal` class as well :

```

public class Meal {

    @Selector(value = "p:nth-child(1)")
    private String name;
    
    @Selector(
        value = "p:nth-child(2)",
        format = "^([0-9.]+)\/5 stars$",
        indexForRegexPattern = 1
    )
    private Float stars;
    
    @Selector(
        value = "p:nth-child(2)",
        // rating-color custom attribute can be used as well
        attr = "rating-color"
    )
    private String ratingColor;

    @Selector(
        value = "p:nth-child(3)"
    )
    private String chefs;
    
    // getters and setters.
}

```

We'll provide more explanations soon on how to build more complex 
POJOs and how some of the features showcased here works.

For the moment, let's see how to scrap this.

```

    
    private static final String MY_HTML_FILE = "my-html-file.html";

    public static void main(String[] args) {
    
    
        HtmlToPojoEngine htmlToPojoEngine = HtmlToPojoEngine.create();

        HtmlAdapter<Restaurant> adapter = htmlToPojoEngine.adapter(Restaurant.class);
        
        // If they were several restaurants in the same page, 
        // you would need to create a parent POJO containing
        // a list of Restaurants as shown with the meals here
        Restaurant restaurant = adapter.fromHtml(getHtmlBody());
        
        // That's it, do some magic now!

    }
    
    
    private static String getHtmlBody() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(MY_HTML_FILE));
        return new String(encoded, Charset.forName("UTF-8"));

    }

```


Another short example can be found [here](https://github.com/Louis-wht/jwht-htmltopojo-booking-example)



# Complete Documentation

## @Selector

`@Selector` annotation is the annotation you need to use on each of
your POJO's fields that you want to be accessed by jwht-htmltopojo
through reflection.


<p>Can be applied to any field of the following types (or their primitive equivalents)</p>
<ul>
    <li>String</li>
    <li>Float</li>
    <li>Double</li>
    <li>Integer</li>
    <li>Long</li>
    <li>Boolean</li>
    <li>java.util.Date</li>
    <li>org.joda.time.DateTime</li>
    <li>org.jsoup.nodes.Element</li>
    <li>Any POJO class annotated with @Selector on fields to populate</li>
    <li>List of supported types</li>
</ul>


>`value` parameter is the main parameter of this annotation. You must populate
it with a [css query](https://jsoup.org/apidocs/org/jsoup/select/Selector.html).
One classic technic to find easily the css query is to open the inspector of your
browser on the html page/file you're trying to convert to a POJO, then select
the tag you want to use, right click > copy > CSS Selector and then pass into the
`value` of your `@Selector` and eventually tweak it if it needs some tweaking. You
can test your CSS selector [here](https://try.jsoup.org/).

>`attr` parameter allows you to define which part of an html tag you want
to use for the corresponding field. "text" is default. Also "html", "innerHtml"
or "outerHtml" is supported. Any other attribute can also be stated but it might
result in null values so be careful not to mistype those. An example of custom 
attr can be found in the above example with `ratingColor` field of `Meal` class.

>`format` regex to use to format the input string. If none is provided, no regex
pattern filter will be used.

>`dateFormat` parameter allows to use define a custom date format to use to 
convert the string to date objects. Depending on if you use standard java 
date or joda time DateTime, please refer to their documentation for date format.
Currently only Java Standard Date field and joda time DateTime fields are 
supported. You can stipulate a locale for the date conversion. (see below).

>`locale` parameter allows to select a locale string, used for Date and Float.

>`defValue` parameter allows you to define a default String value if selected 
HTML element is empty. If your field is not a String, this default string will 
be casted to the required type through the default pipeline for simple types 
fields (Integer, Long, Double, Float, String, Boolean, Date, Element).

>`index` parameter allows you to Index define the index of the HTML element to 
pick. If the css query has several results, then which one should be picked ? 
You can give this information with this parameter.

>`indexForRegexPattern` parameter allows you to choose the index of the regex 
group you want the regex pattern to output. Will only be used if you submitted a 
`format` string. For example, if your regex is as following : `^(Restaurant|Hotel) n\*([0-9]+)$`
and the input string is {@code Restaurant n*912} and you only want `912`, then you
should give this parameter the value `2` to select the second regex group. Another
example can be found above in `Restaurant` class with `id` and `rank` fields 
where both uses the same regex with another `indexForRegexPattern`.

>`returnDefValueOnThrow` parameter allows you to choose to return the default value
in case a parsing exception occures during field processing.

> There are four other parameters that we will explain in the next paragraph.


## Deserializer

### General Knowledge

### Provided Deserializer

### Creating and using a custom Deserializer

## @AcceptObjectIf

### General Knowledge

### Provided AcceptObjectResolver

### Creating and using a custom AcceptObjectResolver

## Injection

## Overriding / Extending Standard API

# Upcoming Additions

# How to contribute


