MACHINE PH

	VARS
		Combine : [0..1]
		NbEssai : [0..2]
		Etat : [0..5]

	INVARIANT
		and(
			Combine ∈ [0..1]
			NbEssai ∈ [0..2]
			Etat ∈ [0..5]
			and(
				and(
					or(
						¬Etat=4
						Combine=0
					)
					or(
						¬Combine=0
						Etat=4
					)
					or(
						¬Etat=4
						NbEssai=0
					)
				)
			)
		)

	INITIALISATION
		Combine := 0 ||
		NbEssai := 0 ||
		Etat := 4

	EVENTS
		Start = 
			SELECT
				and(
					Combine=1
					¬Etat=5
					¬Etat=4
				)
			THEN
				ANY
					e ∈ {3, 6}
				WHERE
					true
				THEN
					IF
						and(
							e=6
							Etat=0
						)
					THEN
						Etat := 1
					ELSE
						IF
							and(
								e=3
								Etat=1
							)
						THEN
							Etat := 3
						ELSE
							IF
								and(
									e=6
									Etat=2
								)
							THEN
								Etat := 1
							ELSE
								SELECT
									and(
										e=6
										Etat=3
									)
								THEN
									Etat := 1
								END
							END
						END
					END
				END
			END

		Timeout = 
			SELECT
				and(
					Combine=1
					¬Etat=5
				)
			THEN
				IF
					Etat=2
				THEN
					Etat := 5
				ELSE
					IF
						NbEssai=2
					THEN
						Etat := 5
					ELSE
						NbEssai := (NbEssai + 1)
					END
				END
			END

		HangUp = 
			SELECT
				or(
					Combine=1
					Etat=5
				)
			THEN
				Etat := 4 ||
				Combine := 0 ||
				NbEssai := 0
			END

		Appel_Qui_Donc = 
			SELECT
				Combine=0
			THEN
				Combine := 1 ||
				Etat := 0
			END

