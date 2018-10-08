MACHINE ELV

	CONSTS
		NE = 4
		LF = (NE - 1)

	SETS
		Floors = [0..LF]
		DoorsState = {ouvertes[0], fermees[1], refermees[2]}

	VARS
		PC : DoorsState
		Pos : Floors
		Dir : {-1, 1}

	FUNS
		BC : Floors -> [0..1]
		BM : Floors -> [0..1]
		BD : Floors -> [0..1]
		PE : Floors -> DoorsState

	INVARIANT
		and(
			NE=4
			LF=(NE - 1)
			PC ∈ DoorsState
			Pos ∈ Floors
			Dir ∈ {-1, 1}
			BC(0) ∈ [0..1]
			BC(1) ∈ [0..1]
			BC(2) ∈ [0..1]
			BC(3) ∈ [0..1]
			BM(0) ∈ [0..1]
			BM(1) ∈ [0..1]
			BM(2) ∈ [0..1]
			BM(3) ∈ [0..1]
			BD(0) ∈ [0..1]
			BD(1) ∈ [0..1]
			BD(2) ∈ [0..1]
			BD(3) ∈ [0..1]
			PE(0) ∈ DoorsState
			PE(1) ∈ DoorsState
			PE(2) ∈ DoorsState
			PE(3) ∈ DoorsState
			and(
				BM(LF)=0
				BD(0)=0
				∀(etage).(and(
					etage ∈ Floors
				) => and(
					etage ≠ Pos
				) => PE(etage)=fermees[1])
				PC=ouvertes[0] => PE(Pos)=ouvertes[0]
				∀(etage).(and(
					etage ∈ Floors
				) => PE(etage) ≠ refermees[2])
			)
		)

	INITIALISATION
		PC := fermees[1] ||
		Pos := 0 ||
		Dir := 1 ||
		PE(0) := fermees[1] ||
		PE(1) := fermees[1] ||
		PE(2) := fermees[1] ||
		PE(3) := fermees[1] ||
		BC(0) := 0 ||
		BC(1) := 0 ||
		BC(2) := 0 ||
		BC(3) := 0 ||
		BM(0) := 0 ||
		BM(1) := 0 ||
		BM(2) := 0 ||
		BM(3) := 0 ||
		BD(0) := 0 ||
		BD(1) := 0 ||
		BD(2) := 0 ||
		BD(3) := 0

	EVENTS
		gAppel_Montee_Etage = 
			ANY
				etage ∈ [0..(LF - 1)]
			WHERE
				BM(etage)=0
			THEN
				BM(etage) := 1
			END

		hAppel_Descente_Etage = 
			ANY
				etage ∈ [1..LF]
			WHERE
				BD(etage)=0
			THEN
				BD(etage) := 1
			END

		eAppel_Montee_Cabine = 
			ANY
				etage ∈ [(Pos + 1)..LF]
			WHERE
				BC(etage)=0
			THEN
				BC(etage) := 1
			END

		fAppel_Descente_Cabine = 
			ANY
				etage ∈ [0..(Pos - 1)]
			WHERE
				BC(etage)=0
			THEN
				BC(etage) := 1
			END

		bOuverture_Portes_Cabine = 
			SELECT
				and(
					PE(Pos)=ouvertes[0]
					PC=fermees[1]
				)
			THEN
				PC := ouvertes[0]
			END

		cFermeture_Portes_Cabine = 
			SELECT
				and(
					PE(Pos)=ouvertes[0]
					PC=ouvertes[0]
				)
			THEN
				PC := refermees[2]
			END

		aOuverture_Portes_Etage = 
			SELECT
				and(
					PE(Pos)=fermees[1]
					PC=fermees[1]
				)
			THEN
				PE(Pos) := ouvertes[0]
			END

		dFermeture_Portes_Etage = 
			SELECT
				and(
					PE(Pos)=ouvertes[0]
					PC=refermees[2]
				)
			THEN
				PE(Pos) := fermees[1]
			END

		iMontee_Cabine = 
			SELECT
				and(
					Pos < LF
					Pos >= 0
					PC=refermees[2]
					PE(Pos)=fermees[1]
					Dir=1
					or(
						∃(etage).(and(
							and(
								etage ∈ [(Pos + 1)..LF]
							)
							BM(etage)=1
						))
						∃(etage).(and(
							and(
								etage ∈ [(Pos + 1)..LF]
							)
							BC(etage)=1
						))
					)
				)
			THEN
				IF
					(Pos + Dir)=LF
				THEN
					Pos := (Pos + Dir) ||
					BC((Pos + Dir)) := 0 ||
					BM((Pos + Dir)) := 0 ||
					PC := fermees[1] ||
					BD(LF) := 0
				ELSE
					Pos := (Pos + Dir) ||
					BC((Pos + Dir)) := 0 ||
					BM((Pos + Dir)) := 0 ||
					PC := fermees[1]
				END
			END

		kDescente_Cabine = 
			SELECT
				and(
					Pos > 0
					Pos <= LF
					PC=refermees[2]
					PE(Pos)=fermees[1]
					Dir=-1
					or(
						∃(etage).(and(
							and(
								etage ∈ [0..(Pos - 1)]
							)
							BD(etage)=1
						))
						∃(etage).(and(
							and(
								etage ∈ [0..(Pos - 1)]
							)
							BC(etage)=1
						))
					)
				)
			THEN
				IF
					(Pos + Dir)=0
				THEN
					Pos := (Pos + Dir) ||
					BC((Pos + Dir)) := 0 ||
					BD((Pos + Dir)) := 0 ||
					PC := fermees[1] ||
					BM(0) := 0
				ELSE
					Pos := (Pos + Dir) ||
					BC((Pos + Dir)) := 0 ||
					BD((Pos + Dir)) := 0 ||
					PC := fermees[1]
				END
			END

		jChangement_Direction = 
			SELECT
				and(
					PC=refermees[2]
					PE(Pos)=fermees[1]
					or(
						and(
							Dir=1
							∀(etage).(and(
								etage ∈ [(Pos + 1)..LF]
							) => and(
								BM(etage)=0
								BC(etage)=0
							))
						)
						and(
							Dir=-1
							∀(etage).(and(
								etage ∈ [0..(Pos - 1)]
							) => and(
								BD(etage)=0
								BC(etage)=0
							))
						)
					)
					or(
						and(
							Dir=-1
							∃(etage).(and(
								and(
									etage ∈ [(Pos + 1)..LF]
								)
								or(
									BM(etage)=1
									BC(etage)=1
								)
							))
						)
						and(
							Dir=1
							∃(etage).(and(
								and(
									etage ∈ [0..(Pos - 1)]
								)
								or(
									BD(etage)=1
									BC(etage)=1
								)
							))
						)
					)
				)
			THEN
				Dir := (-1 * Dir)
			END

