CodePath Week 3 Project

CPTwitterClient Android App Readme

Objective of this project is to create a simple Android Twitter client that supports viewing a Twitter timeline and composing a new tweet.

Time spent: 16-18 hours spent in total Completed user stories:

    Required: User can sign in to Twitter using OAuth login
    Required: User can view the tweets from their home timeline
                User should be displayed the username, name, and body for each tweet
                User should be displayed the relative timestamp for each tweet "8m", "7h"
                User can view more tweets as they scroll with infinite pagination
    Required: User can switch between Timeline and Mention views using tabs.
                User can view their home timeline tweets.
                User can view the recent mentions of their username.
    Required: User can navigate to view their own profile
                User can see picture, tagline, # of followers, # of following, and tweets on their profile.
    Required: User can click on the profile image in any tweet to see another user's profile.
                User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
                Profile view should include that user's timeline
    Required: User can compose a new tweet
               User can click a “Compose” icon in the Action Bar on the top right
               User can then enter a new tweet and post this to twitter
               User is taken back to home timeline with new tweet visible in timeline
    Required: User can scroll down “infinitely” to continue loading more image results (up to 8 pages)

    Optional: Links in tweets are clickable and will launch the web browser
    Optional: User can see a counter with total number of characters left for tweet

    Advanced: User can refresh tweets timeline by pulling down to refresh
    Advanced: User can tap a tweet to display a "detailed" view of that tweet
    Advanced: User can select "reply" from detail view to respond to a tweet
                User that wrote the original tweet is automatically "@" replied in compose

Advanced: User can open the twitter app offline and see last loaded tweets
Tweets are persisted into sqlite and can be displayed from the local DB

    Bonus: Compose activity is replaced with a modal overlay

Walkthrough of all the user stories:

![](https://github.com/Vikramjeet-Singh/CPTwitterClient/blob/master/CPTwitterClient_walkthrough.gif)
