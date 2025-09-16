# RS2 World

RS2 World is an RSPS project which aims to be more of a concept server than a production-ready server.
The goal is to finally "finish" an RSPS project using methods and concepts I've picked up over the years working in IT.

The project is loosely based on blakeman8192's runesource

## Overview

There are 3 asynchronous processes running, each dealing with specific tasks
1. Server - accepting clients, reading and decoding packets and queueing them on the client's packet queue
2. Login - new clients are first put on the login process until the login is complete or failed. Fetching, saving,
serializing and deserializing of save files happens here to not impact the cycles of the other processes
3. World - the actual 600ms game cycle where all game logic occurs

A client will be active on either the world or login process, where the packets queued on its packet queue will be processed.
The design of the codebase is made so that each of these processes are seperated as best as possible. While you will find some objects referencing
objects from the other "modules", communication between these processes generally occurs through Spring Application Events.

## Planned features

1. Scripting support - No hardcoding of content other than frameworking
2. Save file migrations - Lazy (on-login) migrations of save-files
3. Multiple revision support - Abstraction in the right places will allow support for multiple revisions
by using the corresponding Spring profile
4. Direct cache integration - Utilizing game data from the cache wherever possible rather than parsing external data sets
5. Central login/communication server - This will be a separate project which will allow this server to be used as either
a standalone world or a clustered world, with save files being shared and allowing private messages and clan chat between worlds

All of this on top of everything else you would expect such as combat, dialogues, fleshed out skills, etc