MACHINE CM

	CONSTS
		MaxPot = 300
		MaxBal = 200
		MaxCof = 6

	VARS
		Pot : {0, 50, 100, 150, 200, 250, 300}
		Balance : {0, 50, 100, 150, 200}
		CofLeft : [0..MaxCof]
		Status : {off[0], on[1], error[2]}
		AskCof : [0..1]
		AskChange : [0..1]

	INVARIANT
		and(
			MaxPot=300
			MaxBal=200
			MaxCof=6
			Pot ∈ {0, 50, 100, 150, 200, 250, 300}
			Balance ∈ {0, 50, 100, 150, 200}
			CofLeft ∈ [0..MaxCof]
			Status ∈ {off[0], on[1], error[2]}
			AskCof ∈ [0..1]
			AskChange ∈ [0..1]
			and(
				or(
					¬Balance=0
					and(
						AskCof=0
						AskChange=0
					)
				)
				or(
					¬AskChange=1
					and(
						AskCof=0
						Balance > 0
					)
				)
				or(
					¬AskCof=1
					and(
						AskChange=0
						or(
							Balance > 50
							Balance=50
						)
					)
				)
				Pot <= (MaxPot + 50)
			)
		)

	INITIALISATION
		Status := off[0] ||
		Pot := 0 ||
		Balance := 0 ||
		CofLeft := MaxCof ||
		AskCof := 0 ||
		AskChange := 0

	EVENTS
		insert50 = 
			SELECT
				and(
					Status=on[1]
					AskChange=0
					AskCof=0
					MaxBal >= (Balance + 50)
				)
			THEN
				Balance := (Balance + 50)
			END

		insert100 = 
			SELECT
				and(
					Status=on[1]
					AskChange=0
					AskCof=0
					MaxBal >= (Balance + 100)
				)
			THEN
				Balance := (Balance + 100)
			END

		powerUp = 
			SELECT
				and(
					Status=off[0]
					CofLeft > 0
					MaxPot >= Pot
				)
			THEN
				Status := on[1] ||
				Balance := 0 ||
				AskCof := 0 ||
				AskChange := 0
			END

		powerDown = 
			SELECT
				or(
					and(
						Status=on[1]
						AskChange=0
						AskCof=0
						Balance=0
					)
					Status=error[2]
				)
			THEN
				Status := off[0]
			END

		autoOut = 
			SELECT
				Status=on[1]
			THEN
				Status := error[2]
			END

		takePot = 
			SELECT
				and(
					Status=off[0]
					Pot >= (MaxPot - 50)
				)
			THEN
				Pot := 0
			END

		cofReq = 
			SELECT
				and(
					Status=on[1]
					Balance >= 50
					AskCof=0
					AskChange=0
				)
			THEN
				AskCof := 1
			END

		changeReq = 
			SELECT
				and(
					Status=on[1]
					Balance > 0
					AskCof=0
					AskChange=0
				)
			THEN
				AskChange := 1
			END

		addCof = 
			ANY
				x ∈ [1..MaxCof]
			WHERE
				MaxCof >= (CofLeft + x)
			THEN
				SELECT
					and(
						Status=off[0]
						MaxCof > CofLeft
					)
				THEN
					CofLeft := (CofLeft + x)
				END
			END

		serveCof = 
			SELECT
				and(
					Status=on[1]
					Balance >= 50
					AskCof=1
					CofLeft > 0
				)
			THEN
				IF
					and(
						or(
							(Pot + 50) > MaxPot
							CofLeft=1
						)
						Balance > 50
					)
				THEN
					Status := error[2] ||
					AskChange := 1 ||
					AskCof := 0 ||
					Balance := (Balance - 50) ||
					CofLeft := (CofLeft - 1) ||
					Pot := (Pot + 50)
				ELSE
					IF
						or(
							(Pot + 50) > MaxPot
							CofLeft=1
						)
					THEN
						Status := error[2] ||
						AskCof := 0 ||
						Balance := (Balance - 50) ||
						CofLeft := (CofLeft - 1) ||
						Pot := (Pot + 50)
					ELSE
						IF
							Balance > 50
						THEN
							AskChange := 1 ||
							AskCof := 0 ||
							Balance := (Balance - 50) ||
							CofLeft := (CofLeft - 1) ||
							Pot := (Pot + 50)
						ELSE
							AskCof := 0 ||
							Balance := (Balance - 50) ||
							CofLeft := (CofLeft - 1) ||
							Pot := (Pot + 50)
						END
					END
				END
			END

		backBalance = 
			SELECT
				and(
					Status=on[1]
					Balance > 0
					AskChange=1
				)
			THEN
				Balance := 0 ||
				AskChange := 0
			END

