# Unique Usernames Plugin

Saves the username of each joined user and prohibits impersonations.

Does require your server to have a database, you may need to rebuild the plugin when changing the database if the driver is not recognized, simply add your databases dependency to gradle!

## How to use

Simply copy both files ``UniqueUsernamesPlugin.jar`` and ``unique-usernames-config.json`` into your ``<server-folder>/config/mods`` directory

In ``unique-usernames-config.json`` the settings for your database connection must be specified.

Use ``freeusername <username>`` in the server console to free up a username which has already been taken, this kicks the player with the given name from the server if online and deletes the entry from the database.
