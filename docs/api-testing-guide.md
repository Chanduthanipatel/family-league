# Family League API Testing Guide

This document lists the APIs currently implemented in the project, along with request payload formats and one example payload for each write endpoint.

## Base assumptions

- Base URL: `http://localhost:8080`
- Auth type: JWT bearer token
- Login first and copy the `accessToken`
- Use this header for protected APIs:
****
```http
Authorization: Bearer <accessToken>
Content-Type: application/json
```

## Bootstrap admin

Current bootstrap admin configured in `application.properties`:

- Email: `chandu@divami.com`
- Password: `CharanAparna@2022`
- Display name: `chandu`

## 1. Authentication APIs

### 1.1 Register (Signup)

- Method: `POST`
- URL: `/api/v1/auth/register`
- Auth required: `No`

Payload format:

```json
{
  "email": "string",
  "password": "string (min 6 chars)",
  "displayName": "string"
}
```

Example:

```json
{
  "email": "alice@example.com",
  "password": "Alice@123",
  "displayName": "Alice"
}
```

Response: Same as login — returns `accessToken` and user details with role `USER`.

### 1.2 Login

- Method: `POST`
- URL: `/api/v1/auth/login`
- Auth required: `No`

Payload format:

```json
{
  "email": "string",
  "password": "string"
}
```

Example:

```json
{
  "email": "chandu@divami.com",
  "password": "CharanAparna@2022"
}
```

### 1.3 Current user

- Method: `GET`
- URL: `/api/v1/auth/me`
- Auth required: `Yes`

No request body.

## 2. Admin Setup APIs

All APIs in this section require an `ADMIN` token.

### 2.1 Create league

- Method: `POST`
- URL: `/api/v1/admin/leagues`

Payload format:

```json
{
  "code": "string",
  "name": "string",
  "description": "string"
}
```

Example:

```json
{
  "code": "IPL",
  "name": "Indian Premier League",
  "description": "Family league for IPL predictions"
}
```

### 2.2 List leagues

- Method: `GET`
- URL: `/api/v1/admin/leagues`

No request body.

### 2.3 Create season

- Method: `POST`
- URL: `/api/v1/admin/leagues/{leagueId}/seasons`

Payload format:

```json
{
  "code": "string",
  "name": "string",
  "seasonYear": 2026,
  "status": "DRAFT|OPEN|LOCKED|COMPLETED|CLOSED",
  "startsAt": "ISO-8601 timestamp",
  "endsAt": "ISO-8601 timestamp",
  "firstMatchAt": "ISO-8601 timestamp",
  "leaguePredictionLockAt": "ISO-8601 timestamp"
}
```

Example:

```json
{
  "code": "IPL-2026",
  "name": "IPL 2026",
  "seasonYear": 2026,
  "status": "OPEN",
  "startsAt": "2026-03-20T10:00:00Z",
  "endsAt": "2026-05-30T18:00:00Z",
  "firstMatchAt": "2026-03-22T14:00:00Z",
  "leaguePredictionLockAt": "2026-03-22T10:00:00Z"
}
```

### 2.4 List seasons for a league

- Method: `GET`
- URL: `/api/v1/admin/leagues/{leagueId}/seasons`

No request body.

### 2.5 Update season status

- Method: `PATCH`
- URL: `/api/v1/admin/seasons/{seasonId}/status`

Payload format:

```json
{
  "status": "DRAFT|OPEN|LOCKED|COMPLETED|CLOSED"
}
```

Example:

```json
{
  "status": "OPEN"
}
```

### 2.6 Create team

- Method: `POST`
- URL: `/api/v1/admin/teams`

Payload format:

```json
{
  "code": "string",
  "name": "string",
  "shortName": "string",
  "logoUrl": "string"
}
```

Example:

```json
{
  "code": "CSK",
  "name": "Chennai Super Kings",
  "shortName": "CSK",
  "logoUrl": "https://example.com/csk.png"
}
```

### 2.7 List teams

- Method: `GET`
- URL: `/api/v1/admin/teams`

No request body.

### 2.8 Create player

- Method: `POST`
- URL: `/api/v1/admin/players`

Payload format:

```json
{
  "code": "string",
  "fullName": "string",
  "shortName": "string"
}
```

Example:

```json
{
  "code": "DHONI",
  "fullName": "Mahendra Singh Dhoni",
  "shortName": "MS Dhoni"
}
```

### 2.9 List players

- Method: `GET`
- URL: `/api/v1/admin/players`
- Optional query param: `?active=true` to filter only active players

No request body.

### 2.10 Get player by ID

- Method: `GET`
- URL: `/api/v1/admin/players/{playerId}`

No request body.

### 2.11 Update player

- Method: `PUT`
- URL: `/api/v1/admin/players/{playerId}`

Payload format:

```json
{
  "fullName": "string",
  "shortName": "string",
  "active": true
}
```

Example:

```json
{
  "fullName": "Mahendra Singh Dhoni",
  "shortName": "MS Dhoni",
  "active": true
}
```

### 2.12 Assign team to season

- Method: `POST`
- URL: `/api/v1/admin/seasons/{seasonId}/teams`

Payload format:

```json
{
  "teamId": 1,
  "seededPosition": 1
}
```

Example:

```json
{
  "teamId": 1,
  "seededPosition": 1
}
```

### 2.13 List season teams

- Method: `GET`
- URL: `/api/v1/admin/seasons/{seasonId}/teams`

No request body.

### 2.14 Create match

- Method: `POST`
- URL: `/api/v1/admin/seasons/{seasonId}/matches`

Payload format:

```json
{
  "matchNumber": 1,
  "homeTeamId": 1,
  "awayTeamId": 2,
  "venue": "string",
  "startsAt": "ISO-8601 timestamp",
  "predictionLockAt": "ISO-8601 timestamp"
}
```

Example:

```json
{
  "matchNumber": 1,
  "homeTeamId": 1,
  "awayTeamId": 2,
  "venue": "Chennai",
  "startsAt": "2026-03-22T14:00:00Z",
  "predictionLockAt": "2026-03-22T13:00:00Z"
}
```

### 2.15 List matches for a season

- Method: `GET`
- URL: `/api/v1/admin/seasons/{seasonId}/matches`

No request body.

## 3. User Prediction APIs

All APIs in this section require an authenticated token.

### 3.1 Save or update league prediction

- Method: `POST`
- URL: `/api/v1/predictions/seasons/{seasonId}`

Payload format:

```json
{
  "entries": [
    {
      "seasonTeamId": 1,
      "predictedPosition": 1
    }
  ]
}
```

Example:

```json
{
  "entries": [
    {
      "seasonTeamId": 1,
      "predictedPosition": 1
    },
    {
      "seasonTeamId": 2,
      "predictedPosition": 2
    }
  ]
}
```

Note:

- You must submit the full ranking for all teams in that season.
- `seasonTeamId` values come from `GET /api/v1/admin/seasons/{seasonId}/teams`.

### 3.2 Get own league prediction

- Method: `GET`
- URL: `/api/v1/predictions/seasons/{seasonId}/me`

No request body.

### 3.3 List all league predictions after lock

- Method: `GET`
- URL: `/api/v1/predictions/seasons/{seasonId}`

No request body.

Note:

- This works only after `leaguePredictionLockAt`.

### 3.4 Save or update match prediction

- Method: `POST`
- URL: `/api/v1/predictions/matches/{matchId}`

Payload format:

```json
{
  "predictedWinnerTeamId": 1,
  "predictedTossWinnerTeamId": 2,
  "predictedPlayerOfMatchId": 1
}
```

Example:

```json
{
  "predictedWinnerTeamId": 1,
  "predictedTossWinnerTeamId": 2,
  "predictedPlayerOfMatchId": 1
}
```

Note:

- `predictedWinnerTeamId` and `predictedTossWinnerTeamId` must be one of the two match teams.
- `predictedPlayerOfMatchId` must exist in the `player` table.
- There is no player admin API yet, so if you want to test this endpoint fully, player rows must exist in the database first.

### 3.5 Get own match prediction

- Method: `GET`
- URL: `/api/v1/predictions/matches/{matchId}/me`

No request body.

### 3.6 List all match predictions after lock

- Method: `GET`
- URL: `/api/v1/predictions/matches/{matchId}`

No request body.

Note:

- This works only after `predictionLockAt`.

## 4. Result APIs

All admin result APIs require an `ADMIN` token.

### 4.1 Publish match result

- Method: `POST`
- URL: `/api/v1/admin/results/matches/{matchId}`

Payload format:

```json
{
  "winningTeamId": 1,
  "tossWinnerTeamId": 2,
  "playerOfMatchId": 1,
  "resultType": "NORMAL|TIE|NO_RESULT",
  "remarks": "string"
}
```

Example:

```json
{
  "winningTeamId": 1,
  "tossWinnerTeamId": 2,
  "playerOfMatchId": 1,
  "resultType": "NORMAL",
  "remarks": "CSK won by 6 wickets"
}
```

Notes:

- For `NO_RESULT`, `winningTeamId` must be `null`.
- For `NORMAL` and `TIE`, `winningTeamId` is required.

### 4.2 Get match result

- Method: `GET`
- URL: `/api/v1/admin/results/matches/{matchId}`

No request body.

### 4.3 Publish season result

- Method: `POST`
- URL: `/api/v1/admin/results/seasons/{seasonId}`

Payload format:

```json
{
  "status": "PUBLISHED|VERIFIED",
  "items": [
    {
      "seasonTeamId": 1,
      "finalPosition": 1,
      "points": 20,
      "wins": 10,
      "losses": 4,
      "ties": 0
    }
  ]
}
```

Example:

```json
{
  "status": "PUBLISHED",
  "items": [
    {
      "seasonTeamId": 1,
      "finalPosition": 1,
      "points": 20,
      "wins": 10,
      "losses": 4,
      "ties": 0
    },
    {
      "seasonTeamId": 2,
      "finalPosition": 2,
      "points": 18,
      "wins": 9,
      "losses": 5,
      "ties": 0
    }
  ]
}
```

Note:

- You must publish the full final ranking for all teams in the season.
- League-level prediction scoring is not implemented yet; standings are stored only.

### 4.4 Get season result

- Method: `GET`
- URL: `/api/v1/admin/results/seasons/{seasonId}`

No request body.

## 5. Leaderboard APIs

### 5.1 Get season leaderboard

- Method: `GET`
- URL: `/api/v1/leaderboards/seasons/{seasonId}`
- Auth required: `Yes`

No request body.

## Suggested testing order

1. `POST /api/v1/auth/register` (create a normal user)
2. `POST /api/v1/auth/login` (login as admin for setup, or as the registered user for predictions)
3. `POST /api/v1/admin/leagues`
4. `POST /api/v1/admin/leagues/{leagueId}/seasons`
5. `POST /api/v1/admin/teams`
6. `POST /api/v1/admin/players` (create players — needed for match predictions and results)
7. `POST /api/v1/admin/seasons/{seasonId}/teams`
8. `POST /api/v1/admin/seasons/{seasonId}/matches`
9. `POST /api/v1/predictions/seasons/{seasonId}`
10. `POST /api/v1/predictions/matches/{matchId}`
11. `POST /api/v1/admin/results/matches/{matchId}`
12. `GET /api/v1/leaderboards/seasons/{seasonId}`
13. `POST /api/v1/admin/results/seasons/{seasonId}`

## Important note

`predictedPlayerOfMatchId` and `playerOfMatchId` must reference a player `id` from `GET /api/v1/admin/players`.
