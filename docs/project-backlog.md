# Family League Backlog

This backlog converts the current requirements into epics, user stories, and rough story-point estimates. Estimates use Fibonacci sizing and are intended for planning, not commitment.

## Estimation scale

- `1`: trivial configuration or documentation task
- `2`: small isolated implementation
- `3`: moderate API or validation work
- `5`: meaningful feature with persistence and rules
- `8`: cross-cutting feature with security, workflow, or async impact
- `13`: large feature that should be split during sprint planning

## Epic 1: Authentication and Access Control

| ID | User story | Points |
| --- | --- | ---: |
| AUTH-1 | As a user, I want to log in securely so that I can access my predictions and rankings. | 5 |
| AUTH-2 | As an admin, I want admin-only APIs protected so that regular users cannot modify league data. | 5 |
| AUTH-3 | As the system, I want JWT-based authentication so that API access is stateless and secure. | 8 |
| AUTH-4 | As a user, I want invalid or missing authentication to fail with a consistent error response. | 2 |

Epic subtotal: `20`

## Epic 2: User Profile Management

| ID | User story | Points |
| --- | --- | ---: |
| PROF-1 | As a user, I want to view my profile so that I can confirm my account details. | 2 |
| PROF-2 | As a user, I want to update my avatar name so that my public identity is personalized. | 2 |
| PROF-3 | As a user, I want to update my profile image reference so that my profile is recognizable. | 2 |

Epic subtotal: `6`

## Epic 3: League and Season Administration

| ID | User story | Points |
| --- | --- | ---: |
| LEAGUE-1 | As an admin, I want to create and update leagues so that multiple competitions can be managed. | 3 |
| LEAGUE-2 | As an admin, I want to create and update seasons under a league so that each real-world edition is isolated. | 5 |
| LEAGUE-3 | As an admin, I want to transition season states so that predictions and closure are controlled. | 5 |
| LEAGUE-4 | As an admin, I want closed seasons to remain viewable but not editable so that history is preserved. | 3 |

Epic subtotal: `16`

## Epic 4: Team, Player, and Match Management

| ID | User story | Points |
| --- | --- | ---: |
| ROSTER-1 | As an admin, I want to manage teams so that they can be reused across seasons. | 3 |
| ROSTER-2 | As an admin, I want to assign teams to seasons so that a season has its own participants. | 3 |
| ROSTER-3 | As an admin, I want to manage players so that predictions can reference player-of-the-match options. | 5 |
| ROSTER-4 | As an admin, I want to assign players to season teams so that rosters are season-aware. | 5 |
| MATCH-1 | As an admin, I want to schedule matches with start and lock times so that prediction windows can be enforced. | 5 |
| MATCH-2 | As an admin, I want to list and update matches so that the replicated league schedule stays accurate. | 3 |

Epic subtotal: `24`

## Epic 5: User Predictions

| ID | User story | Points |
| --- | --- | ---: |
| PRED-1 | As a user, I want to submit my full season ranking before the deadline so that I can compete in league predictions. | 8 |
| PRED-2 | As a user, I want to update my season ranking until lock so that I can refine my choices. | 5 |
| PRED-3 | As a user, I want to submit match predictions before kickoff so that I can earn match points. | 5 |
| PRED-4 | As a user, I want to edit my match prediction until lock so that I can change my mind in time. | 3 |
| PRED-5 | As a user, I want to view my own saved predictions so that I can verify what the system stored. | 3 |

Epic subtotal: `24`

## Epic 6: Prediction Locking and Visibility

| ID | User story | Points |
| --- | --- | ---: |
| LOCK-1 | As the system, I want season predictions locked 4 hours before the first match so that users are treated fairly. | 5 |
| LOCK-2 | As the system, I want match predictions locked 1 hour before match start so that late changes are prevented. | 5 |
| LOCK-3 | As a user, I want predictions to become read-only after lock so that the rules are transparent. | 3 |
| LOCK-4 | As a user, I want to see others’ predictions only after lock so that no one gains unfair influence. | 5 |
| LOCK-5 | As the system, I want strong persistence-level enforcement for late writes so that race conditions do not bypass rules. | 8 |

Epic subtotal: `26`

## Epic 7: Result Publishing and Scoring

| ID | User story | Points |
| --- | --- | ---: |
| RES-1 | As an admin, I want to publish official match results so that the system can evaluate predictions. | 5 |
| RES-2 | As an admin, I want to publish final season standings so that league-level scoring can be completed. | 5 |
| SCORE-1 | As the system, I want points to be calculated server-side only so that scores cannot be manipulated. | 8 |
| SCORE-2 | As the system, I want scoring to run asynchronously after result publication so that admin workflows stay responsive. | 8 |
| SCORE-3 | As an admin, I want visibility into scoring success or failure so that I can act on processing issues. | 3 |

Epic subtotal: `29`

## Epic 8: Leaderboards

| ID | User story | Points |
| --- | --- | ---: |
| LEAD-1 | As a user, I want to see the current season leaderboard so that I know my standing. | 3 |
| LEAD-2 | As a user, I want my rank and points refreshed after confirmed results so that the competition feels live. | 5 |
| LEAD-3 | As an admin, I want leaderboard recalculation to be repeatable so that corrupted aggregates can be rebuilt. | 5 |
| LEAD-4 | As a user, I want historical leaderboards for closed seasons so that prior competitions remain visible. | 3 |

Epic subtotal: `16`

## Epic 9: Notifications and Communication

| ID | User story | Points |
| --- | --- | ---: |
| NOTIFY-1 | As a user, I want reminder emails before prediction deadlines if I have not submitted picks so that I do not miss participation. | 8 |
| NOTIFY-2 | As an admin, I want alerts when result updates are pending so that scoring is not delayed. | 5 |
| NOTIFY-3 | As an admin, I want to send bulk communication to selected users so that I can announce important events. | 8 |
| NOTIFY-4 | As the system, I want all email activity logged so that notification history is auditable. | 5 |

Epic subtotal: `26`

## Epic 10: Audit, Integrity, and Compliance

| ID | User story | Points |
| --- | --- | ---: |
| AUDIT-1 | As the system, I want important data changes audited so that actions are traceable. | 5 |
| AUDIT-2 | As the system, I want soft delete behavior on domain records so that historical data is preserved. | 3 |
| AUDIT-3 | As the system, I want created and updated metadata populated consistently so that change tracking is reliable. | 3 |
| AUDIT-4 | As the business, I want scores to remain non-editable through APIs so that trust in the platform is maintained. | 3 |

Epic subtotal: `14`

## Epic 11: API Quality and Operational Readiness

| ID | User story | Points |
| --- | --- | ---: |
| OPS-1 | As a developer, I want consistent request and response contracts so that clients are easy to build. | 3 |
| OPS-2 | As an operator, I want global exception handling and useful logs so that runtime issues are diagnosable. | 5 |
| OPS-3 | As a reviewer, I want API documentation and examples so that the platform can be evaluated quickly. | 5 |
| OPS-4 | As a maintainer, I want externalized configuration so that environments can be managed safely. | 3 |
| OPS-5 | As a team, I want a decision log so that ambiguous design choices are recorded with justification. | 2 |

Epic subtotal: `18`

## Epic 12: Testing and Release Readiness

| ID | User story | Points |
| --- | --- | ---: |
| TEST-1 | As a maintainer, I want unit tests around scoring and lock rules so that core logic does not regress. | 8 |
| TEST-2 | As a maintainer, I want integration tests for secured APIs so that auth and RBAC remain reliable. | 8 |
| TEST-3 | As a reviewer, I want the app to start from a clean clone so that the submission is verifiable. | 3 |
| TEST-4 | As a team, I want end-to-end coverage for the main prediction flow so that the system is demonstrably complete. | 8 |

Epic subtotal: `27`

## Planning summary

- Total estimated points across all identified stories: `246`
- Recommended first implementation slice:
  - `AUTH-1`, `AUTH-2`, `AUTH-3`
  - `LEAGUE-1`, `LEAGUE-2`
  - `ROSTER-1`, `ROSTER-2`, `MATCH-1`
  - `PRED-3`, `LOCK-2`
  - `RES-1`, `SCORE-1`
  - `LEAD-1`

That first slice gives a usable vertical path: secure login, admin setup, match prediction, result publishing, and leaderboard update.
