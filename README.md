# Facebook API ME

**Facebook API ME** defines a compelling and well defined API for Java developers who wish to access Facebook's services. This project provides support for the main and more popular services, e.g., status posting, friends list, etc. More services are added as new versions are released. Facebook API ME is defined in such a way that developers will learn how to use it quickly. In addition, the API is structured to work regardless of the underlying Java platform (e.g. Java ME, Android and RIM), in order to make easier and quicker for the developers to port their applications to other platforms.

## Licensing

Facebook API ME is under two licenses: **[GNU General Public License v2.0](http://en.wikipedia.org/wiki/GNU_General_Public_License)** regarding the source code and **[GNU Lesser General Public License v3.0](http://en.wikipedia.org/wiki/GNU_Lesser_General_Public_License)** for the binaries. It means that now you can develop proprietary applications with Facebook API ME, if you merely link them to API's binaries.

## Minimum Requirements

* Java Micro Edition (MIDP 2.0 / CLDC 1.0) or newer
* Android 1.5 (API Level 3) or newer
* RIM OS

## Update History

Facebook API ME is now at its first release (**1.0**). This version consists of:

Version | Date | Contents
------- | ---- | --------
1.0 | 03/06/2011 | 1. Authentication.<br/> 2. Post Status.<br/> 3. Share Link.<br/> 4. Get List of Friends.<br/> 5. Get Profile Picture.<br/> 6. Revoke Authorization.

## Tutorial

Below you will find a tutorial demonstrating what you need and how in order to work with Facebook API ME.

### 1. Registering an App

You need to register an app on **Facebook's Developer** [page](http://developers.facebook.com/). The process is pretty simple and straightforward. The goal of this registration is to obtain some keys that are required by authentication process: **App Id**, **App Secret** and **Redirect Uri**. This process is very similar to Twitter's app registration process.

Once you have the keys, let get to the API itself.

### 2. Browser Component

In your app, you need to display Facebook's Login page, so your user can enter his credentials and then grant access to your app. This process is implemented by the API. You just need to provide a browser component instance and then the API takes care of the rest. Considering the **eSWT** version, you need to provide an instance of **[Browser](http://www.eclipse.org/ercp/eswt/gallery/gallery.php)** class, **Android**, **[WebView](http://developer.android.com/reference/android/webkit/WebView.html)** class, and **RIM**, **[BrowserField](http://www.blackberry.com/developers/docs/5.0.0api/net/rim/device/api/browser/field2/BrowserField.html)** class.

### 3. Wrapper Class

Once the browser instance is created, you have to wrap it with an instance of ***AuthDialogWrapper***  class. This class is responsible for managing the authentication and delegates the events to your app. Considering the eSWT version, you have to instantiate the ***BrowserAuthDialogWrapper*** subclass, Android, ***WebViewAuthDialogWrapper***, and RIM, ***BrowserFieldAuthDialogWrapper***.

### 4. Setting Up the Keys

The wrapper object must be set up, so it can handle the authentication process properly for you app. There are some "set" methods that can be used to inform your App Id, App Secret and Redirect Uri. If you prefer, you can enter the keys in the constructor.

```java
...
Browser browser = ...;

AuthDialogWrapper pageWrapper = new BrowserAuthDialogWrapper(browser);
pageWrapper.setAppId("App Id goes here");
pageWrapper.setAppSecret("App Secret goes here");
pageWrapper.setRedirectUri("Redirect Uri goes here");
...
```

### 5. Permissions

Most services provided by Facebook API requires explicit authorization by user. So, during the authentication process you must inform which permissions the services that you intend to work with, must be granted by user.

```java
...
pageWrapper.setPermissions(
    new String[] {
        Permission.OFFLINE_ACCESS, Permission.PUBLISH_STREAM});
...
```

This code snippet says the app will request permission to perform authorized requests on behalf of the user at any time (**OFFLINE_ACCESS**) and publish content to a user's feed at any time **PUBLISH_STREAM**. For the full list of permissions provided by Facebook API, click [here](http://developers.facebook.com/docs/authentication/permissions/).

### 6. Authentication Events

In order to know if the user granted or denied permission to the app, you need to register a listener of the type ***AuthenticationListener***. This interface has three methods:

* **onAuthorize**(String token)
* **onAccessDenied**(String message)
* **onFail**(String error, String message)

***onAuthorize()*** means the user has granted permission to the app along with the **Access Token**. Keep this token, since you will use it to sign all your requests to Facebook API. This token works to identify your app. So, from this point on, you can dismiss the login page and start publishing links, comments, etc.

***onAccessDenied()*** means the user has denied access to his Facebook account. It does not mean the user can change his mind later and then authorize your app.

***onFail()*** means that something went wrong during the authentication process. Just try again!

```java
...
pageWrapper.addAuthenticationListener(this);
...

public void onAuthorize(String token) {
    System.out.println("onAuthorize: " + token);
}

public void onAccessDenied(String message) {
    System.out.println("access_denied: " + message);
}

public void onFail(String error, String message) {
    System.out.println("error: " + error + " message: " + message);
}
...
```

### 7. Displaying Login Page

The Facebook's Login page will just be requested and displayed, as soon as the method ***login()*** be called.

```java
...
pageWrapper.login();
...
```

### 8. Dispatching Requests

The API also provides a helper class, called ***Dispatcher***, responsible for dispatching the requests, synchronously or asynchronously, to Facebook API. Each Dispatcher instance must be associated to an access token, so all requests dispatched be signed automatically.

```java
...
Dispatcher dispatcher = Dispatcher.getInstance("Access Token goes here");
...
```

### 9. Dispatcher Events

When you work with asynchronous requests, it is recommended to register a listener in order to know whether they were successfully processed. This listener is defined by ***DispatcherListener*** interface. This interface has two methods:

* **onComplete**(Request request, Response response)
* **onFail**(Request request, Throwable error)

***onComplete()*** means the request was processed successfully and returns the result (response).

***onFail()*** means the request failed, returning the cause.

```java
...
dispatcher.addDispatcherListener(this);
...

public void onComplete(Request request, Response response) {
    ...
}

public void onFail(Request request, Throwable error) {
    ...
}
...
```

### 10. Posting on User's Wall

To publish a text on user's wall is pretty simple. Just instantiate an object of ***Status*** class, informing the text in the constructor. The other constructor allows you to post a text on a friend's wall.

```java
...
Status status = new Status("Text goes here");
dispatcher.dispatch(status); //synchronously
//or dispatcher.addToQueue(status); //asynchronously
...
Status status = new Status("Text goes here", "Friend's Id goes here");
dispatcher.dispatch(status);
...
```

### 11. Sharing Link

To share a link is as easy as posting on wall. Just instantiate an object of ***Link*** class, informing the URL in the constructor. The other constructor allows you to define how the link will be displayed.

```java
...
Link link = new Link("Link's URL goes here");
dispatcher.dispatch(link);
...
Link link =
    new Link(
        "Link'URL goes here",
        "Link's Picture Uri goes here",
        "Link's Name goes here", 
        "Link's Caption goes here", 
        "Link's Description goes here", 
        "Link's Message goes here", 
        "Friend's Id goes here");
dispatcher.dispatch(link);
...
```

### 12. Retrieving Friends List

To retrieve the user's friends list just instantiate an object of ***Friends*** class. This request returns an instance of ***Friends.Response***, which is ***Enumeration***.

```java
...
Friends friends = new Friends();
Friends.Response friendsEnum = (Friends.Response) dispatcher.dispatch(friends);

while (friendsEnum.hasMoreElements()) {
    Friends.Response friend = (Friends.Response)friendsEnum.nextElement();
    String friendId = friend.getId();
    String friendName = friend.getName();
    ...
}
...
```

### 13. Profile Picture

To retrieve a friend's profile picture, create an instance of ***Picture*** class, informing the friend's Id in the constructor. This request returns an instance of ***Picture.Response***.

```java
...
Picture picture = new Picture("Friend's Id goes here");
Picture.Response pictureResp = (Picture.Response)dispatcher.dispatch(picture);
byte[] imageBytes = pictureResp.getData();
...
```

### 14. Revoking Authorization

Just in case an user decides to remove your app's authorization to access his Facebook account. For that, use the request defined by ***RevokeAuthorization*** class, informing the access token in the constructor. Once this request is processed, the access token is expired and you app will no longer be able to access the user's account. To access it again, the entire authentication process must be redone.

```java
...
RevokeAuthorization revoke = new RevokeAuthorization("Access Token goes here");
dispatcher.dispatch(revoke);
...
```

### 15. Log out

The wrapper class ***AuthDialogWrapper*** has a method called ***logout()***. Call this method in order to remove any cookie or logged user's data left by Facebook's Login page in our browser component. It is recommend to call this method when the user removes the app's authorization.

### Other Platforms

It is important to point out that all sceneries presented in this tutorial are valid for all platforms of Facebook API ME. It does matter if you are working on Blackberry, LWUIT or Android. The sceneries are all the same, except **Wrapper Class**, regarding the type of subclass. Each platform has its own subclass of *AuthDialogWrapper*.

## Donation

In case of Facebook API ME has brought good benefits for you and/or your company, and because of that you would like to thank us for all our hard work. Please, feel free to donate us any amount, via PayPal, by clicking **[here](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=ernandes%40gmail%2ecom&lc=US&item_name=Facebook%20API%20ME&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest)**. It is easy and quick to do. In addition, this contribution will provide us more resources to keep up improving this API for you.

## See Also

* None

## References

* None

## External Links

* [J2ME Group Blog](http://j2megroup.blogspot.com)
* [eMob Tech](http://www.emobtech.com)
