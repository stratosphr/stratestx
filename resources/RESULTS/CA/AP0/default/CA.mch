MACHINE CA

	VARS
		Be : [0..1]
		AC : [0..1]
		Do : [0..1]
		Lo : [0..1]
		Wa : [0..1]
		Us : [0..1]
		Gl : [0..1]
		CS : [0..1]
		Li : [0..1]
		Mv : [0..1]
		Tr : [0..1]
		De : [0..10]

	INVARIANT
		and(
			Be ∈ [0..1]
			AC ∈ [0..1]
			Do ∈ [0..1]
			Lo ∈ [0..1]
			Wa ∈ [0..1]
			Us ∈ [0..1]
			Gl ∈ [0..1]
			CS ∈ [0..1]
			Li ∈ [0..1]
			Mv ∈ [0..1]
			Tr ∈ [0..1]
			De ∈ [0..10]
			and(
				or(
					¬and(
						AC=1
						Us=1
					)
					Do=0
				)
				or(
					¬Be=1
					AC=1
				)
				or(
					¬AC=0
					Be=0
				)
				or(
					¬Wa=1
					Be=1
				)
				or(
					¬Wa=0
					Be=0
				)
				or(
					¬Lo=1
					or(
						Do=0
						Us=0
					)
				)
				or(
					¬Us=1
					Be=0
				)
				or(
					¬Lo=1
					Gl=0
				)
				or(
					¬Be=1
					Gl=0
				)
				or(
					¬Mv=1
					Do=0
				)
				or(
					¬Mv=1
					AC=0
				)
				or(
					¬Mv=1
					Us=1
				)
				or(
					¬Mv=1
					and(
						De=0
						Tr=0
					)
				)
				or(
					¬Li=1
					Lo=0
				)
				or(
					¬Li=1
					AC=0
				)
				or(
					¬Li=1
					Tr=0
				)
				or(
					¬Tr=1
					Lo=1
				)
			)
		)

	INITIALISATION
		Do := 0 ||
		Lo := 0 ||
		AC := 0 ||
		Be := 0 ||
		Li := 0 ||
		Wa := 0 ||
		De := 0 ||
		Tr := 0 ||
		Mv := 0 ||
		CS := 0 ||
		Gl := 0 ||
		Us := 1

	EVENTS
		Be_Activation = 
			SELECT
				and(
					Tr=0
					Mv=0
					AC=1
					Do=1
				)
			THEN
				Wa := 1 ||
				Be := 1
			END

		Be_Stop = 
			SELECT
				and(
					Tr=0
					Mv=0
					Be=1
					AC=0
				)
			THEN
				Wa := 0 ||
				Be := 0
			END

		Doors_Opening = 
			SELECT
				and(
					Tr=0
					Mv=0
					Do=0
					or(
						Us=0
						and(
							Us=1
							Lo=0
							AC=0
						)
					)
				)
			THEN
				Do := 1
			END

		Doors_Closing = 
			SELECT
				and(
					Tr=0
					Mv=0
					Do=1
					Us=1
				)
			THEN
				Do := 0
			END

		User_Authorized = 
			SELECT
				and(
					Tr=0
					Mv=0
					Us=0
					Be=1
				)
			THEN
				Us := 1 ||
				Be := 0 ||
				Wa := 0 ||
				AC := 0 ||
				Lo := 0
			END

		User_Unauthorized = 
			SELECT
				and(
					Tr=0
					Mv=0
					Us=1
					Do=0
					AC=1
					Lo=1
				)
			THEN
				Us := 0
			END

		Alarm_Deactivation = 
			SELECT
				and(
					Tr=0
					Mv=0
					Us=1
					AC=1
				)
			THEN
				AC := 0
			END

		Alarm_Activation = 
			SELECT
				and(
					Tr=1
					Mv=0
					Lo=1
					Do=0
					AC=0
					De > 4
					11 > De
				)
			THEN
				AC := 1 ||
				Tr := 0 ||
				De := 0
			END

		Doors_Locking = 
			SELECT
				and(
					Tr=0
					Mv=0
					Do=0
					Us=1
					Lo=0
					Gl=0
					AC=0
				)
			THEN
				Lo := 1 ||
				Li := 0 ||
				Tr := 1 ||
				De := 0
			END

		Doors_Unlocking = 
			SELECT
				and(
					Tr=0
					Lo=1
					Do=0
					Us=1
				)
			THEN
				Lo := 0
			END

		Glasses_Opening = 
			SELECT
				and(
					Tr=0
					Gl=0
					Lo=0
				)
			THEN
				Gl := 1
			END

		Glasses_Closing = 
			SELECT
				and(
					Tr=0
					Gl=1
					Lo=0
				)
			THEN
				Gl := 0
			END

		Ch_Sec_Activation = 
			SELECT
				and(
					Tr=0
					Mv=0
					Lo=0
					CS=0
					Do=1
				)
			THEN
				CS := 1
			END

		Ch_Sec_Deactivation = 
			SELECT
				and(
					Tr=0
					Mv=0
					Lo=0
					CS=1
					Do=1
				)
			THEN
				CS := 0
			END

		Car_Moving = 
			SELECT
				and(
					Tr=0
					Mv=0
					Do=0
					Lo=0
					AC=0
				)
			THEN
				Mv := 1
			END

		Car_Stopping = 
			SELECT
				and(
					Tr=0
					Mv=1
					Do=0
					Lo=0
					AC=0
				)
			THEN
				Mv := 0
			END

		Light_Activation = 
			SELECT
				and(
					Tr=0
					Li=0
					Lo=0
					AC=0
				)
			THEN
				Li := 1
			END

		Light_Deactivation = 
			SELECT
				and(
					Tr=0
					Li=1
					Lo=0
					AC=0
				)
			THEN
				Li := 0
			END

		Increment_Chronometer = 
			SELECT
				and(
					Tr=1
					10 > De
				)
			THEN
				De := (De + 1)
			END

