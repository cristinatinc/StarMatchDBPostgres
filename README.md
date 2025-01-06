# 🌠 StarMatch - Astrology App

![image](https://github.com/user-attachments/assets/3687d561-ea23-4018-9610-290534dd022f)

## Table of Contents 🌟

1. [Application Description]
2. [Main Features]
3. [Technologies Used]

## 1. Application Description 🌟
#### ✨StarMatch✨ is an astrology-based application, designed to inspire self-discovery and build meaningful friendships.

By using astrological charts as its foundation, the app calculates compatibility 
for friendships and offers personalized insights into users' personalities and 
relationships. By gathering information about the user's birthday, StarMatch generates a detailed natal chart, 
highlighting sun, moon, and rising signs. Users can connect with others, explore their astrological 
profiles, and enjoy tailored quotes, traits, and compatibility analyses for deeper connections and self-awareness.
   
## 2. Main Features 🌟
### ♈ User Profile Management : 
- **Sign-Up and Sign-In:** Users can create an account by providing an email address, a password, their birthdate, birth time, and birth location.
- **Profile Updates:** Users can modify their profile information at any time, ensuring accuracy and personalization.
- **Log-In:** By entering their email and password, users can easily log back into their profiles.

### ♉ Personalized Natal Charts : 
The app generates a detailed natal chart for each user, highlighting their:
* **Sun Sign** (representing a person's core personality traits):
     Determined using the user's birth date. The app checks the day and month to assign the corresponding zodiac sign based on traditional astrological date ranges.
* **Moon Sign** (representing a person's inner feelings):
     The app uses a 2.5-day lunar cycle to determine the position of the moon and maps it to a zodiac sign.
* **Rising Sign** (representing how one is perceived from the outside):
     Determined using the user's birth time. The 24-hour day is divided into 12 zodiac segments, with each sign lasting for about 2 hours.

### ♊ Compatibility analysis : 
The compatibility analysis in StarMatch is a core feature that calculates compatibility scores between a user and their friends based on their astrological charts. It uses a nuanced algorithm  to evaluate the harmony between their sun, moon, and rising signs, along with the elemental balance of those signs.
Here are the steps to the calculation process:
* **Retrieving Friend Information and their Natal Charts**
* **Elemental Compatibility Check** (by applying predefined compatibility rules: Signs with the same element are inherently compatible. Certain elemental pairings (e.g., Fire and Air, Earth and Water) are also considered harmonious.)
* **Binary Representation for Star Signs**
* **Calculating Compatibility Scores**
* **Normalizing the Final Score**

### ♌ Personality Traits Insight: 
By accessing this feature, users receive a list of personality traits tailored to their major astrological placements, helping them understand themselves better.
This app will analyze their natal chart and return a list of traits associated to their elemental placements and signs.

### ♍ Personalized Quotes: 
Generates an inspiring quote based on the user's sun sign and its elemental affiliation (Fire, Earth, Air, or Water), ensuring motivation.

### ♋ Connecting with friends :
The social aspect of StarMatch is another one of its core aspects. The app provides its users with the opportunity to connect with others in order to find out the compatibility score of the relationship.
Users can:
- **add** friends to their friend list by looking up their emails
- **remove** friends from their friend list

### ♎ Local Networking :
Users can search for friends who were born in the same city as them, to have the opportunity to expand the community connections. This encourages local community-building and shared experiences, while also simplifying the user experience. 

### ♏ Astrological Trends Analysis : 
By accessing the data, the app identifies the most common astrological elements within the community, 
offering the users a snapshot of the statistics.

### ♐ Admin App Management : 
An option reserved for admins of StarMatch, the application provides such users with exclusive tools to manage the database, ensuring continuous freshness.:
- add, remove and update the **traits**
- add, remove and update the **quotes**
- add, remove and update the **users**
- add, remove and update other **admins**

## 3. Technologies Used 🌟
Built on a 4-layer architecture, StarMatch offers three storage opportunities through the implementation of a Repository Interface: in Memory, in File, and using a Database. The storage type can be selected at the beginning.
- **Programming Language:** Java
- **Testing Framework:** JUnit
- **Build Tool:** Maven
- **Database:** PostgreSQL
- **File Storage:** CSV

