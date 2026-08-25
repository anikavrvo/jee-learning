CREATE SCHEMA IF NOT EXISTS member;
CREATE SCHEMA IF NOT EXISTS plan;
CREATE SCHEMA IF NOT EXISTS authorizations;

CREATE OR REPLACE FUNCTION public.set_updated_at()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

---------------------------------------------------------------------
-- Replicated/imported data
---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS member.member (
    member_id       INT GENERATED ALWAYS AS IDENTITY,
    member_number   VARCHAR(20)  NOT NULL,
    email           VARCHAR(255),
    phone           VARCHAR(10),
    member_status   VARCHAR(20)  NOT NULL DEFAULT 'INACTIVE',
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_member
        PRIMARY KEY (member_id),

    CONSTRAINT uq_member_member_number
        UNIQUE (member_number),

    CONSTRAINT ck_member_status
        CHECK (member_status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE IF NOT EXISTS plan.plan (
    plan_id         INT GENERATED ALWAYS AS IDENTITY,
    plan_code       VARCHAR(30)  NOT NULL,
    plan_name       VARCHAR(100) NOT NULL,
    plan_description TEXT,
    plan_status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_plan
        PRIMARY KEY (plan_id),

    CONSTRAINT uq_plan_code
        UNIQUE (plan_code),

    CONSTRAINT ck_plan_status
        CHECK (plan_status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE IF NOT EXISTS plan.benefit (
    benefit_id      INT GENERATED ALWAYS AS IDENTITY,
    benefit_code    VARCHAR(50)  NOT NULL,
    benefit_name    VARCHAR(100) NOT NULL,
    benefit_description TEXT,
    benefit_status  VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_benefit
        PRIMARY KEY (benefit_id),

    CONSTRAINT uq_benefit_code
        UNIQUE (benefit_code),

    CONSTRAINT ck_benefit_status
        CHECK (benefit_status IN ('ACTIVE', 'INACTIVE'))
);

-- Policies taken out by members
CREATE TABLE IF NOT EXISTS member.member_plan (
    member_plan_id  INT GENERATED ALWAYS AS IDENTITY,
    member_id       INT      NOT NULL,
    plan_id         INT      NOT NULL,
    member_plan_start_date      DATE     NOT NULL,
    member_plan_end_date        DATE,
    member_plan_status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_member_plan
        PRIMARY KEY (member_plan_id),

    CONSTRAINT fk_member_plan_member
        FOREIGN KEY (member_id)
        REFERENCES member.member (member_id),

    CONSTRAINT fk_member_plan_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan.plan (plan_id),

    CONSTRAINT ck_member_plan_status
        CHECK (member_plan_status IN ('ACTIVE', 'SUSPENDED', 'TERMINATED'))

    -- CONSTRAINT ck_member_plan_dates
    --     CHECK (
    --         member_plan_end_date IS NULL
    --         OR member_plan_end_date >= member_plan_start_date
    --     )
);

-- Benefits attached to given plan codes
CREATE TABLE IF NOT EXISTS plan.plan_benefit (
    plan_benefit_id       INT GENERATED ALWAYS AS IDENTITY,
    plan_id               INT       NOT NULL,
    benefit_id            INT       NOT NULL,
    coverage_percentage   NUMERIC(5,2),
    coverage_limit        NUMERIC(12,2),
    waiting_period_days   INTEGER      NOT NULL DEFAULT 0,
    requires_authorization BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at            TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_plan_benefit
        PRIMARY KEY (plan_benefit_id),

    CONSTRAINT fk_plan_benefit_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan.plan (plan_id),

    CONSTRAINT fk_plan_benefit_benefit
        FOREIGN KEY (benefit_id)
        REFERENCES plan.benefit (benefit_id),

    CONSTRAINT uq_plan_benefit
        UNIQUE (plan_id, benefit_id),

    CONSTRAINT ck_plan_benefit_coverage_percentage
        CHECK (
            coverage_percentage IS NULL
            OR coverage_percentage BETWEEN 0 AND 100
        ),

    CONSTRAINT ck_plan_benefit_coverage_limit
        CHECK (
            coverage_limit IS NULL
            OR coverage_limit >= 0
        ),

    CONSTRAINT ck_plan_benefit_waiting_period
        CHECK (waiting_period_days >= 0)
);


---------------------------------------------------------------------
-- Managed authorization data
---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS authorizations.authorization_request (
    authorization_request_id INT GENERATED ALWAYS AS IDENTITY,
    member_id                INT NOT NULL,
    plan_id                  INT NOT NULL,
    requested_at             TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    procedure_description    TEXT NOT NULL,
    request_status           VARCHAR(20) NOT NULL DEFAULT 'APPROVED',
    created_at               TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_authorization_request
        PRIMARY KEY (authorization_request_id),

    CONSTRAINT fk_authorization_request_member
        FOREIGN KEY (member_id)
        REFERENCES member.member (member_id),

    CONSTRAINT fk_authorization_request_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan.plan (plan_id),

    CONSTRAINT ck_authorization_request_status
        CHECK (request_status IN ('APPROVED', 'REJECTED'))
);

CREATE TABLE IF NOT EXISTS authorizations.authorization_response (
    authorization_response_id INT GENERATED ALWAYS AS IDENTITY,
    authorization_request_id  INT NOT NULL,
    response_status           VARCHAR(20) NOT NULL,
    response_reason           TEXT,
    responded_at              TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_authorization_response
        PRIMARY KEY (authorization_response_id),

    CONSTRAINT fk_authorization_response_request
        FOREIGN KEY (authorization_request_id)
        REFERENCES authorizations.authorization_request (authorization_request_id),

    CONSTRAINT ck_authorization_response_status
        CHECK (response_status IN ('APPROVED', 'PENDING', 'REJECTED'))
);

DROP TRIGGER IF EXISTS trg_member_set_updated_at ON member.member;
CREATE TRIGGER trg_member_set_updated_at
    BEFORE UPDATE ON member.member
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

DROP TRIGGER IF EXISTS trg_plan_set_updated_at ON plan.plan;
CREATE TRIGGER trg_plan_set_updated_at
    BEFORE UPDATE ON plan.plan
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

DROP TRIGGER IF EXISTS trg_benefit_set_updated_at ON plan.benefit;
CREATE TRIGGER trg_benefit_set_updated_at
    BEFORE UPDATE ON plan.benefit
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

DROP TRIGGER IF EXISTS trg_member_plan_set_updated_at ON member.member_plan;
CREATE TRIGGER trg_member_plan_set_updated_at
    BEFORE UPDATE ON member.member_plan
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();

DROP TRIGGER IF EXISTS trg_plan_benefit_set_updated_at ON plan.plan_benefit;
CREATE TRIGGER trg_plan_benefit_set_updated_at
    BEFORE UPDATE ON plan.plan_benefit
    FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


---------------------------------------------------------------------
-- Sample data
---------------------------------------------------------------------

INSERT INTO member.member (member_number, email, phone, member_status)
VALUES ('M001', 'john.doe@example.com', '0882345678', 'ACTIVE');

INSERT INTO plan.plan (plan_code, plan_name, plan_description, plan_status)
VALUES ('P001', 'Basic Health Plan', 'Basic health coverage plan', 'ACTIVE');   

INSERT INTO member.member_plan (member_id, plan_id, member_plan_start_date, member_plan_status)
VALUES (1, 1, '2023-01-01', 'ACTIVE');