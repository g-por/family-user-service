-- V2: Refactor users table, remove roles

-- 1) Prepare defaults and data fixes
DO $$ BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_extension WHERE extname='pgcrypto') THEN
    CREATE EXTENSION pgcrypto;
  END IF;
END $$;

-- 2) Rename full_name -> nickname, fill nulls, set NOT NULL
ALTER TABLE users RENAME COLUMN full_name TO nickname;
UPDATE users SET nickname = COALESCE(nickname, 'user-' || substr(id::text,1,8));
ALTER TABLE users ALTER COLUMN nickname SET NOT NULL;

-- 3) Add phone with UA pattern, backfill placeholder for existing rows, set NOT NULL
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone TEXT;
UPDATE users SET phone = COALESCE(phone, '+380000000000');
ALTER TABLE users ALTER COLUMN phone SET NOT NULL;
-- Add/replace CHECK constraint for Ukrainian numbers +380XXXXXXXXX (9 digits after +380)
DO $$ BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.table_constraints 
    WHERE constraint_name = 'chk_users_phone_ua' AND table_name = 'users'
  ) THEN
    ALTER TABLE users DROP CONSTRAINT chk_users_phone_ua;
  END IF;
END $$;
ALTER TABLE users ADD CONSTRAINT chk_users_phone_ua CHECK (phone ~ '^\+380\d{9}$');

-- 4) Set default avatar URL and backfill NULLs
ALTER TABLE users ALTER COLUMN avatar_url SET DEFAULT 'https://api.dicebear.com/7.x/identicon/svg?seed=family-budget';
UPDATE users SET avatar_url = 'https://api.dicebear.com/7.x/identicon/svg?seed=family-budget' WHERE avatar_url IS NULL;

-- 5) Drop role relations and tables
DO $$ BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.table_constraints 
    WHERE constraint_type='FOREIGN KEY' AND table_name='user_roles'
  ) THEN
    ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS user_roles_user_id_fkey;
    ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS user_roles_role_id_fkey;
  END IF;
EXCEPTION WHEN undefined_table THEN
  -- ignore
END $$;

DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
