create table app_role (
    id bigserial primary key,
    code varchar(50) not null unique,
    name varchar(100) not null,
    description varchar(255),
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false
);

create table app_user (
    id bigserial primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    display_name varchar(120) not null,
    status varchar(30) not null default 'ACTIVE',
    last_login_at timestamptz,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_app_user_status check (status in ('ACTIVE', 'INACTIVE', 'LOCKED'))
);

create table user_profile (
    id bigserial primary key,
    user_id bigint not null unique references app_user(id),
    avatar_name varchar(120),
    profile_image_url varchar(500),
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false
);

create table app_user_role (
    id bigserial primary key,
    user_id bigint not null references app_user(id),
    role_id bigint not null references app_role(id),
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_app_user_role unique (user_id, role_id)
);

create table league (
    id bigserial primary key,
    code varchar(50) not null unique,
    name varchar(120) not null,
    description text,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false
);

create table season (
    id bigserial primary key,
    league_id bigint not null references league(id),
    code varchar(50) not null unique,
    name varchar(120) not null,
    season_year integer not null,
    status varchar(30) not null default 'DRAFT',
    starts_at timestamptz,
    ends_at timestamptz,
    first_match_at timestamptz,
    league_prediction_lock_at timestamptz,
    closed_at timestamptz,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_season_status check (status in ('DRAFT', 'OPEN', 'LOCKED', 'COMPLETED', 'CLOSED'))
);

create table team (
    id bigserial primary key,
    code varchar(50) not null unique,
    name varchar(120) not null,
    short_name varchar(20) not null,
    logo_url varchar(500),
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false
);

create table season_team (
    id bigserial primary key,
    season_id bigint not null references season(id),
    team_id bigint not null references team(id),
    seeded_position integer,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_season_team unique (season_id, team_id)
);

create table player (
    id bigserial primary key,
    code varchar(50) not null unique,
    full_name varchar(150) not null,
    short_name varchar(80),
    active boolean not null default true,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false
);

create table team_player (
    id bigserial primary key,
    season_team_id bigint not null references season_team(id),
    player_id bigint not null references player(id),
    joined_at timestamptz,
    left_at timestamptz,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_team_player unique (season_team_id, player_id)
);

create table league_match (
    id bigserial primary key,
    season_id bigint not null references season(id),
    home_team_id bigint not null references team(id),
    away_team_id bigint not null references team(id),
    match_number integer,
    venue varchar(255),
    starts_at timestamptz not null,
    prediction_lock_at timestamptz not null,
    status varchar(30) not null default 'SCHEDULED',
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_league_match_status check (status in ('SCHEDULED', 'LOCKED', 'RESULT_PENDING', 'RESULT_PUBLISHED', 'CANCELLED')),
    constraint chk_league_match_teams check (home_team_id <> away_team_id),
    constraint chk_match_lock_before_start check (prediction_lock_at <= starts_at)
);

create table league_prediction (
    id bigserial primary key,
    season_id bigint not null references season(id),
    user_id bigint not null references app_user(id),
    submitted_at timestamptz not null default current_timestamp,
    locked_at_snapshot timestamptz not null,
    status varchar(30) not null default 'SUBMITTED',
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_league_prediction unique (season_id, user_id),
    constraint chk_league_prediction_status check (status in ('SUBMITTED', 'LOCKED'))
);

create table league_prediction_item (
    id bigserial primary key,
    league_prediction_id bigint not null references league_prediction(id) on delete cascade,
    season_team_id bigint not null references season_team(id),
    predicted_position integer not null,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_league_prediction_item_position unique (league_prediction_id, predicted_position),
    constraint uk_league_prediction_item_team unique (league_prediction_id, season_team_id)
);

create table match_prediction (
    id bigserial primary key,
    league_match_id bigint not null references league_match(id),
    user_id bigint not null references app_user(id),
    predicted_winner_team_id bigint references team(id),
    predicted_toss_winner_team_id bigint references team(id),
    predicted_player_of_match_id bigint references player(id),
    submitted_at timestamptz not null default current_timestamp,
    locked_at_snapshot timestamptz not null,
    status varchar(30) not null default 'SUBMITTED',
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_match_prediction unique (league_match_id, user_id),
    constraint chk_match_prediction_status check (status in ('SUBMITTED', 'LOCKED'))
);

create table match_result (
    id bigserial primary key,
    league_match_id bigint not null unique references league_match(id),
    winning_team_id bigint references team(id),
    toss_winner_team_id bigint references team(id),
    player_of_match_id bigint references player(id),
    result_type varchar(30) not null default 'NORMAL',
    published_by bigint not null references app_user(id),
    published_at timestamptz not null default current_timestamp,
    remarks text,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_match_result_type check (result_type in ('NORMAL', 'TIE', 'NO_RESULT'))
);

create table season_result (
    id bigserial primary key,
    season_id bigint not null unique references season(id),
    published_by bigint not null references app_user(id),
    published_at timestamptz not null default current_timestamp,
    status varchar(30) not null default 'PUBLISHED',
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_season_result_status check (status in ('PUBLISHED', 'VERIFIED'))
);

create table season_result_item (
    id bigserial primary key,
    season_result_id bigint not null references season_result(id) on delete cascade,
    season_team_id bigint not null references season_team(id),
    final_position integer not null,
    points integer,
    wins integer,
    losses integer,
    ties integer,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_season_result_position unique (season_result_id, final_position),
    constraint uk_season_result_team unique (season_result_id, season_team_id)
);

create table score_event (
    id bigserial primary key,
    season_id bigint not null references season(id),
    user_id bigint not null references app_user(id),
    league_match_id bigint references league_match(id),
    event_type varchar(30) not null,
    points_awarded integer not null,
    source_ref varchar(100),
    processed_at timestamptz not null default current_timestamp,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_score_event_type check (event_type in ('MATCH_WINNER', 'TOSS_WINNER', 'PLAYER_OF_MATCH', 'LEAGUE_POSITION'))
);

create table leaderboard_entry (
    id bigserial primary key,
    season_id bigint not null references season(id),
    user_id bigint not null references app_user(id),
    total_points integer not null default 0,
    rank_position integer,
    last_recalculated_at timestamptz,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_leaderboard_entry unique (season_id, user_id)
);

create table notification_campaign (
    id bigserial primary key,
    created_by_user_id bigint not null references app_user(id),
    campaign_type varchar(30) not null,
    event_type varchar(50) not null,
    subject varchar(255) not null,
    message_body text not null,
    scheduled_at timestamptz,
    sent_at timestamptz,
    status varchar(30) not null default 'DRAFT',
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_notification_campaign_type check (campaign_type in ('SYSTEM', 'MANUAL')),
    constraint chk_notification_campaign_status check (status in ('DRAFT', 'SCHEDULED', 'SENT', 'FAILED'))
);

create table notification_campaign_recipient (
    id bigserial primary key,
    notification_campaign_id bigint not null references notification_campaign(id) on delete cascade,
    user_id bigint not null references app_user(id),
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint uk_notification_campaign_recipient unique (notification_campaign_id, user_id)
);

create table email_log (
    id bigserial primary key,
    notification_campaign_id bigint references notification_campaign(id),
    user_id bigint references app_user(id),
    recipient_email varchar(255) not null,
    event_type varchar(50) not null,
    subject varchar(255) not null,
    body text not null,
    status varchar(30) not null default 'PENDING',
    requested_at timestamptz not null default current_timestamp,
    sent_at timestamptz,
    failure_reason text,
    created_at timestamptz not null default current_timestamp,
    created_by varchar(100) not null default 'system',
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(100) not null default 'system',
    is_deleted boolean not null default false,
    constraint chk_email_log_status check (status in ('PENDING', 'SENT', 'FAILED', 'SKIPPED'))
);

create table audit_log (
    id bigserial primary key,
    entity_name varchar(100) not null,
    entity_id varchar(100) not null,
    action varchar(30) not null,
    actor_user_id bigint references app_user(id),
    actor_name varchar(120),
    change_summary text,
    old_value jsonb,
    new_value jsonb,
    created_at timestamptz not null default current_timestamp,
    constraint chk_audit_log_action check (action in ('CREATE', 'UPDATE', 'DELETE', 'RESTORE', 'LOGIN', 'PUBLISH', 'CLOSE'))
);

create index idx_app_user_status on app_user(status);
create index idx_season_league on season(league_id);
create index idx_season_status on season(status);
create index idx_season_team_season on season_team(season_id);
create index idx_team_player_season_team on team_player(season_team_id);
create index idx_league_match_season on league_match(season_id);
create index idx_league_match_starts_at on league_match(starts_at);
create index idx_league_match_lock_at on league_match(prediction_lock_at);
create index idx_match_prediction_user on match_prediction(user_id);
create index idx_league_prediction_user on league_prediction(user_id);
create index idx_score_event_season_user on score_event(season_id, user_id);
create index idx_leaderboard_entry_season_rank on leaderboard_entry(season_id, rank_position);
create index idx_email_log_status on email_log(status);
create index idx_audit_log_entity on audit_log(entity_name, entity_id);

insert into app_role (code, name, description)
values
    ('ADMIN', 'Administrator', 'Platform administrator with league management privileges'),
    ('USER', 'User', 'Regular participant in prediction leagues');
