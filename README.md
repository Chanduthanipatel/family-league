# 🏏 Family League - IPL Example Workflow

## 📌 Overview

This document explains how the **Family League backend system** works using the **IPL (Indian Premier League)** as an example.

The system allows users to:
- Predict match outcomes
- Predict final league standings
- Earn points based on accuracy
- Compete on a leaderboard

---

## 🧩 Core Concept

> Real IPL happens → Users predict → System evaluates → Points awarded → Leaderboard updated

---

## ⚙️ Step 1: Admin Setup

Admin creates the entire IPL structure in the system:

- League → IPL
- Season → IPL 2026
- Teams → CSK, MI, RCB, etc.
- Matches → Same as real IPL schedule

📌 Note:
The system mirrors the real IPL schedule exactly.

---

## 👤 Step 2: User Predictions

### 🏆 2.1 League-Level Prediction

Before IPL starts, users predict final rankings:

Example:
1. CSK
2. MI
3. RCB
   ...
10. DC

⏱ Lock Rule:
- Closes **4 hours before first match**

---

### 🏏 2.2 Match-Level Prediction

Before each match (e.g., CSK vs MI):

User predicts:
- Match Winner → CSK
- Toss Winner → MI
- Player of the Match → Dhoni

⏱ Lock Rule:
- Closes **1 hour before match start**

---

## 🔒 Step 3: Prediction Lock & Visibility

Before lock:
- ❌ Users cannot see others’ predictions

After lock:
- ✅ Users can view others’ predictions (head-to-head comparison)

---

## 🏟 Step 4: Real Match Happens

Example result:
- Winner → CSK
- Toss → MI
- Player of Match → Dhoni

---

## 👨‍💼 Step 5: Admin Updates Results

Admin enters actual results into the system.

📌 Important:
- No automatic scraping
- Manual entry by admin

---

## ⚙️ Step 6: Scoring Logic

System calculates points:

- Each correct prediction → +1 point

Example:
- 3 correct predictions → +3 points
- 1 correct prediction → +1 point

📌 Rule:
- Points are **calculated only by system**
- Even admin cannot modify points

---

## 📊 Step 7: Leaderboard Update

After each match:
- Points updated
- Rankings recalculated

Example:

| Rank | User   | Points |
|------|--------|--------|
| 1    | User A | 10     |
| 2    | User B | 8      |
| 3    | User C | 7      |

---

## 🔁 Step 8: Repeat Flow
For every match:

## 🏁 Step 9: End of League

After IPL ends:

- Admin updates final standings
- Final leaderboard is calculated
- League is **closed**

---

## 🔐 Step 10: Post-Closure Behavior

After closing:
- ✅ Data is viewable
- ❌ No edits allowed (even by admin)

---

## 🧠 Key System Rules

### ⏱ Time-Based Rules
- League prediction → closes 4 hours before first match
- Match prediction → closes 1 hour before match

### 🔐 Security Rules
- JWT authentication
- Role-based access (Admin/User)
- Prediction lock enforced at DB level

### 📧 Notification Rules
- Users get reminders before prediction closes
- Admin gets alerts for result updates

### 🧾 Data Integrity
- No hard deletes (soft delete only)
- Audit logs for all changes
- Emails stored in database

---

## 🔥 Mental Model

There are 3 parallel timelines:

1. Real IPL timeline (matches happening)
2. User prediction timeline (before lock)
3. System processing timeline (scoring & leaderboard)

---

## 🚀 Summary

This system is a **prediction engine layered on top of IPL**, where:

- Users compete using predictions
- System ensures fairness via locks and rules
- Admin controls data
- Leaderboard drives engagement

---

