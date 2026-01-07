# Energy Generation Mix - Backend
Backend service for the Energy Generation Mix application for Great Britain.

Live page: https://energy-mix.onrender.com

This application uses public API **Carbon Intensity API** to analyze daily UK's energy generation mix and find the most eco-friendly window to charge a car

## Features

### Daily Energy Generation Mix
- Fetches generation mix for today, tomorrow and the day after tomorrow
- Calculates clean energy percentage for each day

## Best Charging Window
- Finds the best charging window within the next 2 days
- Maximizes average clean energy % during the chosen charging period

## Tech stack
- Java 17
- Spring Boot
- Gradle
- Docker
- Carob Intensity API

## How to run with Docker
docker build -t energy-mix-backend . 
docker run -p 8080:8080 energy-mix-backend

