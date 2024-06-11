# Introduction
This is a project demonstrating an issue with TokenCredentials.create() where it seems like the initial result is cached.

# The issue
If you cold start your app without any Internet connection, TokenCredentials.create() will obviously fail since it requires Internet connection.
Now while the app is in the foreground and you turn on Internet connection again then retry to load the map, then TokenCredentials.create() will fail every time.

To me it seems like the initial error message from TokenCredentials.create() is cached somehow and will fail until you kill the application process and restart it.

# Video showing the problem
<video src="https://github.com/erlingfjelstad/esri-auth-to-map/assets/4091353/5398764c-36e7-40c0-9a68-cb3e7e2dc5a6"></video>




# Description of what's happening in the video
Firstly I demonstrate that we manage to successfully authenticate to the map while we have Internet connectivity.
Then I turn off all Internet connection on the emulator and cold start the application. 
After the cold start of the application, obviously we cannot authenticate, and TokenCredentials.create() fails as expected.
Then I go and enable the Internet connection on the emulator again and retry to load the map, which fails.

To prove that my emulator has Internet connection, I open Chrome and google some stuff just to see we get results.
Then I go back into my application and retry to load the map, which still fails with the same exception that we don't have any Internet connection.

# Reproduce the issue
In local.properties you need to add constants to authenticate to your ArcGIS portal. These constants are required:

USERNAME=""

PASSWORD=""

PORTAL_URL = ""

PORTAL_ITEM_ID = ""

# Map implementation
Map composable is copied from https://github.com/Esri/arcgis-maps-sdk-kotlin-toolkit/tree/main/toolkit/geoview-compose

