# 📘 Redis — Setup & Usage Guide
## 🧱 1️⃣ Run Redis with Docker
```
docker run -d --name redis-server -p 6379:6379 redis:7.2
```



This will:

Pull Redis 7.2 if not already downloaded.

Start a container named redis-server.

Expose Redis on port 6379 (default port).

Check if it’s running:

docker ps


Access Redis CLI:

docker exec -it redis-server redis-cli

## 🌍 2️⃣ Using Redis GEO Commands

Redis GEO commands let you store and query geospatial data (latitude/longitude).

➕ Add a location
```
GEOADD cities <longitude> <latitude> <name>
```



Example:
```
GEOADD cities 80.2707 13.0827 "Chennai"
GEOADD cities 77.5946 12.9716 "Bangalore"
GEOADD cities 72.8777 19.0760 "Mumbai"
```


📍 Get position of a location
```
GEOPOS cities "Chennai"
```

Output:

1) 1) "80.270699977874756"
2) "13.082699775695801"

📏 Get distance between two locations
```
GEODIST cities "Chennai" "Bangalore" km
```

Output:

"290.5278"


🔍 Find locations within a radius
```
GEORADIUSBYMEMBER cities "Chennai" 300 km
```

Returns all cities within 300 km of Chennai.

Or use specific coordinates:

```
GEOSEARCH cities FROMLONLAT 80.2707 13.0827 BYRADIUS 300 km
```

## 🧹 3️⃣ Manage and inspect data

Show all keys:
```
KEYS *
```

Check approximate memory usage:
```
MEMORY USAGE cities
```

Check how many cities are stored:

```
ZCARD cities
```

## 🧠 4️⃣ Notes

GEO data is internally stored as a Sorted Set (ZSET).

Each member’s score encodes the latitude/longitude.

Redis GEO is very fast and memory-efficient for radius or proximity queries.
