-- Function to process tournament join (check capacity, deduct fee)
CREATE OR REPLACE FUNCTION process_tournament_join(
    p_tournament_id UUID,
    p_user_id UUID
)
RETURNS jsonb
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    v_tournament tournaments%ROWTYPE;
    v_user_balance DECIMAL(10, 2);
    v_result jsonb;
BEGIN
    -- Get tournament details
    SELECT * INTO v_tournament
    FROM tournaments
    WHERE id = p_tournament_id;

    -- Check if tournament exists
    IF NOT FOUND THEN
        RETURN jsonb_build_object('success', false, 'message', 'Tournament not found');
    END IF;

    -- Check if tournament is open for registration
    IF v_tournament.status != 'REGISTRATION_OPEN' AND v_tournament.status != 'UPCOMING' THEN
        RETURN jsonb_build_object('success', false, 'message', 'Tournament registration is closed');
    END IF;

    -- Check if tournament is full
    IF v_tournament.current_participants >= v_tournament.max_participants THEN
        RETURN jsonb_build_object('success', false, 'message', 'Tournament is full');
    END IF;

    -- Check if user already joined
    IF EXISTS (
        SELECT 1 FROM tournament_participants
        WHERE tournament_id = p_tournament_id AND user_id = p_user_id
    ) THEN
        RETURN jsonb_build_object('success', false, 'message', 'Already joined this tournament');
    END IF;

    -- Check user balance
    SELECT wallet_balance INTO v_user_balance
    FROM users
    WHERE id = p_user_id;

    IF v_user_balance < v_tournament.entry_fee THEN
        RETURN jsonb_build_object('success', false, 'message', 'Insufficient balance');
    END IF;

    -- Deduct entry fee
    IF v_tournament.entry_fee > 0 THEN
        UPDATE users
        SET wallet_balance = wallet_balance - v_tournament.entry_fee
        WHERE id = p_user_id;

        -- Create transaction record
        INSERT INTO wallet_transactions (
            user_id,
            amount,
            type,
            category,
            description,
            reference_id,
            status
        ) VALUES (
            p_user_id,
            v_tournament.entry_fee,
            'DEBIT',
            'TOURNAMENT_ENTRY',
            'Entry fee for ' || v_tournament.title,
            p_tournament_id::TEXT,
            'COMPLETED'
        );
    END IF;

    -- Add participant
    INSERT INTO tournament_participants (tournament_id, user_id)
    VALUES (p_tournament_id, p_user_id);

    -- Update tournament participant count
    UPDATE tournaments
    SET current_participants = current_participants + 1
    WHERE id = p_tournament_id;

    RETURN jsonb_build_object('success', true, 'message', 'Successfully joined tournament');
END;
$$;

// ===== Test Data SQL =====
-- Insert sample tournaments for testing
INSERT INTO tournaments (
    title, game, description, rules, image_url,
    entry_fee, prize_pool, max_participants, team_size,
    start_time, end_time, registration_deadline, status
) VALUES
(
    'PUBG Mobile Championship',
    'PUBG Mobile',
    'Join the ultimate battle royale tournament! Show your skills and win amazing prizes.',
    '1. No emulator allowed\n2. Squad mode only\n3. TPP perspective\n4. All maps included',
    'https://images.unsplash.com/photo-1542751371-adc38448a05e',
    0, 10000, 100, 4,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '2 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '3 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '1 day')) * 1000,
    'REGISTRATION_OPEN'
),
(
    'Free Fire Pro League',
    'Free Fire',
    'Battle it out in the Free Fire arena! Compete for glory and cash prizes.',
    '1. Solo mode\n2. Classic mode only\n3. No VPN allowed\n4. Fair play strictly enforced',
    'https://images.unsplash.com/photo-1511512578047-dfb367046420',
    50, 5000, 50, 1,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '3 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '4 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '2 days')) * 1000,
    'UPCOMING'
),
(
    'COD Mobile Tournament',
    'Call of Duty Mobile',
    'Fast-paced action awaits! Join the COD Mobile tournament.',
    '1. Team Deathmatch\n2. Squad size: 5\n3. Best of 3 rounds\n4. No hacking/modding',
    'https://images.unsplash.com/photo-1552820728-8b83bb6b773f',
    100, 15000, 80, 5,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '5 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '6 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '4 days')) * 1000,
    'UPCOMING'
),
(
    'Valorant Showdown',
    'Valorant',
    'Prove your tactical skills in this intense Valorant competition.',
    '1. 5v5 format\n2. Standard competitive rules\n3. All agents allowed\n4. Map pool: All current maps',
    'https://images.unsplash.com/photo-1538481199705-c710c4e965fc',
    0, 20000, 64, 5,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '7 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '8 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '6 days')) * 1000,
    'REGISTRATION_OPEN'
),
(
    'Clash Royale Cup',
    'Clash Royale',
    'Strategic card battles! Compete in the Clash Royale championship.',
    '1. 1v1 ladder format\n2. Best of 5\n3. No card restrictions\n4. Tournament standard levels',
    'https://images.unsplash.com/photo-1556438064-2d7646166914',
    25, 3000, 32, 1,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '4 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '5 days')) * 1000,
    EXTRACT(EPOCH FROM (NOW() + INTERVAL '3 days')) * 1000,
    'UPCOMING'
);

-- Insert sample global chat room
INSERT INTO chat_rooms (type, name, participants) VALUES
('GLOBAL', 'Global Chat', ARRAY[]::UUID[]);
