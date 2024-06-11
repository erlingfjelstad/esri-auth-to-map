# Introduction
This is a project demonstrating an issue with TokenCredentials.create() where it seems like the initial result is cached.

# The issue
If you cold start your app without any Internet connection, TokenCredentials.create() will obviously fail since it requires Internet connection.
Now while the app is in the foreground and you turn on Internet connection again then retry to load the map, then TokenCredentials.create() will fail every time.

To me it seems like the initial error message from TokenCredentials.create() is cached somehow and will fail until you kill the application process and restart it.

# Map implementation
Map composable is copied from https://github.com/Esri/arcgis-maps-sdk-kotlin-toolkit/tree/main/toolkit/geoview-compose

