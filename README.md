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

Please note that the `HtmlToPojoEngine` provides a cache of
`HtmlAdapter` for faster parsing time. It is recommended to
reuse the same `HtmlToPojoEngine` for your whole application 
in most cases. If used with Spring framework for example, you
could declare it as a bean and later reuse it anywhere with 
the magic `@Autowired` annotation.

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
the tag you want to use, right click > copy > CSS Selector and then past it into
the `value` of your `@Selector` and eventually tweak it if it needs some tweaking.
You can test your CSS selector [here](https://try.jsoup.org/).

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
and the input string is `Restaurant n*912` and you only want `912`, then you
should give this parameter the value `2` to select the second regex group. Another
example can be found above in `Restaurant` class with `id` and `rank` fields 
where both uses the same regex with another `indexForRegexPattern`. A great tool
to test your regex and choose the correct `indexForRegexPattern` can be found
[here](https://regex101.com/).

>`returnDefValueOnThrow` parameter allows you to choose to return the default value
in case a parsing exception occures during field processing.

> There are four other parameters that we will explain in the next paragraph.


## Deserializer


### General Knowledge


An Html Deserializer can be used to define deserialization hooks.
 
There is two different deserialization processes, pre and post
deserialization.

-   Pre deserialization happens just after the raw string value
    has been gathered from the HTML element, it must return a
    string.

-   Post deserialization happens after regex matching and pre
    deserialization and must return an object whose type converts
    back to the field's type.
    
An HTML deserializer can only be used on simple fields (Integer,
Long, Double, Float, String, Boolean, Date, Element) or on list
of simple elements fields. Otherwise it won't get called.
  
To use an Html Deserializer on one of your fields, you should process
as following :
 
  ```
      @Selector(
          value = "some-css-query",
          useDeserializer = true,
          // if you want the pre conversion method to be called
          preConvert = true,
          // if you want the post conversion method to be called
          postConvert = true,
          deserializer = MyCustomDeserializer.class
      )
      private String myDeserializedString;
  ```

### Provided Deserializer

This lib comes with 4 out of the box serializer :

#### TextLengthSelectorDeserializer :

This one helps you to retrieve easily the first chars, words or
sentences of a given input string.

You have to provide an `@TextLengthSelector` annotation on 
top of the corresponding field in order for this deserializer
to work properly.

Here is an example of how to use this Deserializer, more
functionalities can be seen from the source class 
`TextLengthSelector`.

```
    @Selector(
          value = "some-css-query",
          
          useDeserializer = true,
          preConvert = true,
          deserializer = TextLengthSelectorDeserializer.class
    )
    @TextLengthSelector(
        length = 3,
        countWith = CountWith.SENTENCE
    )    
    // This string will contain maximum the 3 first sentences
    // of the original input sentence
    private String myQuiteLongPreviewString;
        
```

#### StringConcatenatorDeserializer

This one will concatenate your string with a static before /
after value. You have to provide an `@StringConcatenator` 
annotation on top of the corresponding field in order for it
to work properly. This can be particularly helpful if you're
trying to use a link to another HTTP ressource and an id is 
hidden somewhere in an HTML tag. You can then concatenate before
and after this id to build a full valid url.


```
    @Selector(
          value = "some-css-query",
          // some other parameters to retrieve only the id
          // ...
          
          useDeserializer = true,
          postConvert = true,
          deserializer = StringConcatenatorDeserializer.class
    )
    @StringConcatenator(
        before = "https://example.com/some-entity/",
        after = "?someParam=someValue"
    )
    // Will result for example in https://example.com/some-entity/1725?someParam=someValue 
    private String myUrl;
        
```

#### ReplacerDeserializer

This implementation provided out of the box will replace any
valid regex pattern matched with another static string provided
on top of the corresponding field with an `@ReplaceWith`
annotation.


```
    @Selector(
          value = "some-css-query",
          // some other parameters to retrieve only a correct number
          // ...
          
          useDeserializer = true,
          preConvert = true,
          deserializer = ReplacerDeserializer.class
    )
    @ReplaceWith(
        value = ",",
        with = ""
    )
    // Will remove from an unparsable number such as 7,456 the , 
    // so that it can correctly become 7456
    private Integer myNumber;
        
```


#### StringConcatenatorAndReplacerDeserializer


This implementation of `HtmlDeserializer` provided out of the 
box compiles a string [replacer](#replacerdeserializer) with 
a [concatenator](#stringconcatenatordeserializer).

You can use it by combining both the `@ReplaceWith` and
the `@StringConcatenator` annotations.


### Creating and using a custom Deserializer

You can of course provide your own Deserializer implementations.
To do so, you only need to implement `HtmlDeserializer` class 
and then refer to your class in the `deserializer` parameter
of an `@Selector`. Your Deserializer can also use its 
custom annotation as does our built in implementations.

Other custom annotations can be retrieved via reflection in the
`init` method where the origin field is provided.

Here is an example of a possible custom implementation :

```
public class CustomHtmlDeserializer implements HtmlDeserializer<String> {


    @Override
    public String deserializePreConversion(String value) throws ConversionException {
        // Do some pre-conversion here (or return the value directly)
    }

    @Override
    public String deserializePostConversion(String value) throws ConversionException {
        // Do some post-conversion here (or return the value directly)
    }

    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {
        // Here you can : store those object if needed in the conversion step, or search for
        // an annotation in the field and store it in the object scope
    }
}
```


## @AcceptObjectIf

### General Knowledge

Sometimes, some field should not be set or some elements 
in a list of other elements should not be parsed. This is 
a quite common use case so we decided to include it somewhere
in our API.

To use such filter, the implementation is really pretty straight
forward : 

```
     @Selector(/*some stuff in here*/)
     @AcceptObjectIf(MyCustomAcceptIfResolver.class)
     // Works with any supported field data type, 
     // not necessarily a POJO altough it is usually
     // the most common use case.
     private SomePojo myConditionalPojoField;
```

Accepting an object happens really early in the processing chain,
even before using the css query of the field itself. That's why
you will retrieve an `Element` (jsoup Html Node).

### Provided AcceptObjectResolver

There is two default implementations provided yet with this 
library. 


#### AcceptIfValidAttrRegexCheck

This one will validate a field only if the submitted regex
matches with the input string. It requires usage of 
`@AttrRegexCheck` annotation on top of your field.

You can use it as following :

```
     @Selector(/*some stuff in here*/)
     @AcceptObjectIf(AcceptIfValidAttrRegexCheck.class)
     @AttrRegexCheck(
        value = "^someRegexCheck$",
        // some custom attr to make the check more challenging
        attr = "item-id"
     )
     private SomePojo myConditionalPojoField;
```

#### AcceptIfFirst


This one will only keep some results out of all inside a given
List of elements.  You can basically give a start and an end 
index to pick from the list. Very useful when you only need 
for example the first three elements of a list.

You have to provide an `@FilterFirstResultsOnly` annotation on
top of the corresponding field in order for this deserializer
to work properly.

You can use it as following :


```
     @Selector(/*some stuff in here*/)
     @AcceptObjectIf(AcceptIfFirst.class)
     @FilterFirstResultsOnly(
        start = 0,
        end = 5
     )
     // This will pick only the first 5 elements of the list
     private List<SomePojo> myList;
```


### Creating and using a custom AcceptObjectResolver


You can of course provide your own `AcceptObjectResolver` 
implementations. To do so, you only need to implement 
`AcceptObjectResolver` class and then refer to your class
in the `value` parameter of an `@AcceptObjectIf`. Your 
custom `AcceptObjectResolver` can also use its custom 
annotation as does our built in implementations.


Other custom annotations can be retrieved via reflection in the
`init` method where the origin field is provided.

Here is an example of a possible custom implementation :


```
public class BookingEndorsementFilter implements AcceptIfResolver {
    @Override
    public boolean accept(Element element, Object parentObject) {

        Elements endorsementName = element.select("div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > p:nth-child(1)");
        return !endorsementName.get(0).text().toLowerCase().contains("city");
    }

    @Override
    public void init(Field field, Object parentObject, Selector selector) throws ObjectCreationException {

    }
}

```

This implementation can be found in [this example project](https://github.com/Louis-wht/jwht-htmltopojo-booking-example)
and will filter out all endorsements whose names contains the 
word "city". This a pretty stupid implementation in this 
case but it features a great example of how this could be used.

## Injection

Any framework needs a decent injection system. Here, it is mostly
thought as to inject other POJO / parent objects into children
one. This can prove to be really useful with more complex and
custom `HtmlDeserializer` or `AcceptObjectResolver`. The 
uses cases for this injections patterns really appeared when
we built [our scrapping framework](https://github.com/whimtrip/jwht-scrapper)
where we couldn't do much without such tool.

There are three injections annotations we will describe here.

### @InjectParent

`@InjectParent` annotation will simply inject parent POJO
in child POJO annotated field. Be careful, this field must
have the same type as your parent POJO's type.


### @Inject and @Injected

`@Inject` works in collaboration with `@Injected`. `@Injected`
has to be set on any parent POJO field to inject in a child 
POJO field annotated with `@Inject`.

As one might want to inject several fields from a Parent POJO
to a child POJO, you must give a "name" to the injection both
within the `@Inject` and `@Injected` annotation so that 
`@Injected` fields will only inject fields values in `@Inject`
fields if both injection name are the same.

Additionnally, parent and child field must have the same type
to avoid any type casting issue.

Below is a correct example :

```

public class ParentPOJO {

    @Injected("inject-me")
    private String toBeInjectedString;
    
}


public class ChildPOJO {

    @Inject("inject-me")
    private String injectedString;
    
}        

```

Here `toBeInjectedString` field value of `ParentPOJO` will 
be injected into `injectedString` field of `ChildPOJO` because
they share both the same type and same injection name 
`inject-me`.

## Overriding / Extending Standard API

Overriding the Standard API can be made in several ways.
The most easy one is to instanciate your `HtmlToPojoEngine`
with a custom `HtmlAdapterFactory` implementations. This
factory will provide a factory method to create `HtmlAdapter`
so that you can provide your own implementation of the interface,
or extends the `DefaultHtmlAdapterImpl` to provide some custom
or additional logic.

You can also implement your own `HtmlField` implementation for 
even more in depth modifications.

# Upcoming Additions

At the moment I am writing those lines, the main thing that
needs to be added to this project is correct Unit Tests. Because
of a lack of time, we couldn't provide real Unit Tests. This 
is the first thing we want to add to this library.

Please feel free to submit your suggestions.


# How to contribute

If you find a bug, an error in the documentation or any other 
related problem, you can submit an issue or even propose a 
patch. 

Your pull requests will be evaluated properly but please submit
decent commented code we won't have to correct and rewrite from
scratch.

We are open to suggestions, code rewriting for optimization, 
etc...

If anyone wants to help, we'd really appreciate if related Unit
tests could be written first and before all to avoid further 
problem.

Thanks for using jwht-htmltopojo! Hope to hear from you!
