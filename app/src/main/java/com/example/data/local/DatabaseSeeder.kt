package com.example.data.local

import com.example.data.local.entity.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object DatabaseSeeder {
    private val seedMutex = Mutex()

    suspend fun seedDatabase(db: AppDatabase) {
        seedDatabaseIfEmpty(db)
    }

    suspend fun seedDatabaseIfEmpty(db: AppDatabase) {
        seedMutex.withLock {
            if (db.memberDao().getMemberCount() > 0) return
            // 1. Members: Seed 150 members
        val memberList = mutableListOf<MemberEntity>()
        val primaryMembers = listOf(
            Triple("M001", "Suresh M", "+91 98765 43210"),
            Triple("M002", "Kumar R", "+91 98765 43211"),
            Triple("M003", "Ravi K", "+91 98765 43212"),
            Triple("M004", "Anand S", "+91 98765 43213"),
            Triple("M005", "Balaji V", "+91 98765 43214"),
            Triple("M006", "Dinesh P", "+91 98765 43215"), // Inactive
            Triple("M007", "Ganesan K", "+91 98765 43216"),
            Triple("M008", "Hariharan S", "+91 98765 43217"),
            Triple("M009", "Jayakumar M", "+91 98765 43218"),
            Triple("M010", "Karthik R", "+91 98765 43219"),
            Triple("M011", "Loganathan T", "+91 98765 43220"),
            Triple("M012", "Murugan S", "+91 98765 43221"),
            Triple("M013", "Natarajan P", "+91 98765 43222"),
            Triple("M014", "Palani V", "+91 98765 43223"),
            Triple("M015", "Ramasamy K", "+91 98765 43224"),
            Triple("M016", "Saravanan D", "+91 98765 43225"),
            Triple("M017", "Thangavel M", "+91 98765 43226"),
            Triple("M018", "Ulaganathan S", "+91 98765 43227"),
            Triple("M019", "Venkatesan R", "+91 98765 43228"),
            Triple("M020", "Yuvaraj G", "+91 98765 43229")
        )

        val colors = listOf("#0D5C3F", "#006D40", "#004D40", "#1B5E20", "#33691E", "#00796B")

        primaryMembers.forEachIndexed { index, (id, name, phone) ->
            val isInactive = (id == "M006")
            memberList.add(
                MemberEntity(
                    memberId = id,
                    name = name,
                    mobile = phone,
                    email = "${name.lowercase().replace(" ", "")}@gmail.com",
                    dob = if (id == "M001") "15-05-1985" else "10-06-198${(index % 9)}",
                    weddingDate = "10-06-2010",
                    bloodGroup = if (index % 2 == 0) "B+ Positive" else "O+ Positive",
                    education = if (id == "M001") "B.Com (Madras Univ)" else "B.Sc / Diploma",
                    occupation = if (id == "M001") "Textile Business" else "Business / Handloom",
                    address = if (id == "M001") "No. 45, Gandhi Bazaar Street, Thorapadi, Vellore, Tamil Nadu - 632002"
                              else "No. ${index + 10}, Sannathi Street, Thorapadi, Vellore - 632002",
                    isActive = !isInactive,
                    isOfficeBearer = (id == "M001" || id == "M015" || id == "M013"),
                    designation = when (id) {
                        "M015" -> "President"
                        "M013" -> "Vice President"
                        "M001" -> "Treasurer"
                        else -> ""
                    },
                    avatarInitials = name.take(2).uppercase(),
                    avatarColorHex = colors[index % colors.size]
                )
            )
        }

        // Generate remaining up to 150 members to fulfill exact 150 members count
        val tamilNames = listOf("Prakash", "Senthil", "Velu", "Arumugam", "Chandran", "Devan", "Elango", "Govind", "Illango", "Jeeva", "Kannan", "Manickam", "Navin", "Prabhu", "Rajesh", "Sekar", "Thirumal", "Vasanth", "Vijayan", "Babu")
        val initials = listOf("A", "B", "C", "D", "G", "K", "M", "N", "P", "R", "S", "T", "V")
        for (i in 21..150) {
            val formattedId = "M" + i.toString().padStart(3, '0')
            val name = "${tamilNames[(i) % tamilNames.size]} ${initials[(i * 3) % initials.size]}"
            val isInactive = (i in listOf(28, 45, 62, 88, 105, 127, 149)) // exactly 8 inactive total (including M006)
            memberList.add(
                MemberEntity(
                    memberId = formattedId,
                    name = name,
                    mobile = "+91 98765 ${43200 + i}",
                    email = "member$i@devangar.org",
                    dob = "12-04-198${(i % 10)}",
                    weddingDate = "20-05-201${(i % 10)}",
                    bloodGroup = "O+ Positive",
                    education = "Graduate",
                    occupation = "Weaving & Business",
                    address = "Street ${i % 12 + 1}, Devangar Colony, Thorapadi, Vellore",
                    isActive = !isInactive,
                    isOfficeBearer = false,
                    avatarInitials = name.take(2).uppercase(),
                    avatarColorHex = colors[i % colors.size]
                )
            )
        }
        db.memberDao().insertMembers(memberList)

        // 2. Family Members for M001 (Suresh M)
        db.familyMemberDao().insertFamilyMembers(
            listOf(
                FamilyMemberEntity(
                    memberId = "M001",
                    name = "Priya S",
                    relationship = "Spouse",
                    dob = "12-08-1990",
                    education = "M.Sc",
                    weddingAnniversary = "10-06-2010"
                ),
                FamilyMemberEntity(
                    memberId = "M001",
                    name = "Rohit S",
                    relationship = "Son",
                    dob = "05-11-2012",
                    education = "Student (8th Std)",
                    weddingAnniversary = ""
                ),
                FamilyMemberEntity(
                    memberId = "M001",
                    name = "Ananya S",
                    relationship = "Daughter",
                    dob = "18-03-2016",
                    education = "Student (5th Std)",
                    weddingAnniversary = ""
                )
            )
        )

        // 3. Office Bearers
        db.officeBearerDao().insertOfficeBearers(
            listOf(
                OfficeBearerEntity(
                    memberId = "M015",
                    name = "K. Ramasamy",
                    designation = "President",
                    phone = "+91 98765 43224",
                    term = "2024-2027",
                    avatarInitials = "KR"
                ),
                OfficeBearerEntity(
                    memberId = "M013",
                    name = "P. Natarajan",
                    designation = "Vice President",
                    phone = "+91 98765 43222",
                    term = "2024-2027",
                    avatarInitials = "PN"
                ),
                OfficeBearerEntity(
                    memberId = "M001",
                    name = "M. Suresh",
                    designation = "Treasurer",
                    phone = "+91 98765 43210",
                    term = "2024-2027",
                    avatarInitials = "SM"
                ),
                OfficeBearerEntity(
                    memberId = "M002",
                    name = "R. Kumar",
                    designation = "Secretary",
                    phone = "+91 98765 43211",
                    term = "2024-2027",
                    avatarInitials = "RK"
                )
            )
        )

        // 4. Funds configuration
        db.fundDao().insertFunds(
            listOf(
                FundEntity(
                    fundCode = "COMMUNITY_FUND",
                    title = "Monthly Community Contribution",
                    category = "COMMUNITY",
                    description = "General community maintenance and welfare contribution",
                    isFixed = true,
                    fixedAmount = 100.0,
                    startDate = "01-01-2026",
                    status = "Active",
                    connectionType = "COMBINED",
                    totalCollected = 14200.0,
                    collectionRate = 95
                ),
                FundEntity(
                    fundCode = "SAVINGS_FESTIVAL_2026",
                    title = "Festival Savings Scheme 2026",
                    category = "SAVINGS",
                    description = "Savings accumulated for annual Deepavali celebration",
                    isFixed = true,
                    fixedAmount = 500.0,
                    startDate = "01-01-2026",
                    status = "Active",
                    connectionType = "COMBINED",
                    totalCollected = 65000.0,
                    collectionRate = 92
                ),
                FundEntity(
                    fundCode = "SAVINGS_MEDICAL_EMERGENCY",
                    title = "Emergency Medical Fund",
                    category = "SAVINGS",
                    description = "Discretionary relief fund for medical emergencies",
                    isFixed = false,
                    fixedAmount = 0.0,
                    startDate = "15-02-2026",
                    status = "Active",
                    connectionType = "SEPARATE",
                    totalCollected = 45000.0,
                    collectionRate = 85
                ),
                FundEntity(
                    fundCode = "SAVINGS_EDUCATION_YOUTH",
                    title = "Education & Youth Fund",
                    category = "SAVINGS",
                    description = "Educational scholarships and sports support for student members",
                    isFixed = true,
                    fixedAmount = 200.0,
                    startDate = "01-03-2026",
                    status = "Active",
                    connectionType = "COMBINED",
                    totalCollected = 30000.0,
                    collectionRate = 78
                ),
                FundEntity(
                    fundCode = "FESTIVAL_DEEPAVALI_2026",
                    title = "Festival - Deepavali 2026",
                    category = "FESTIVAL",
                    description = "Grand Deepavali celebration fund and feast",
                    isFixed = true,
                    fixedAmount = 1000.0,
                    startDate = "01-08-2026",
                    status = "Active",
                    connectionType = "COMBINED",
                    totalCollected = 135000.0,
                    collectionRate = 90
                ),
                FundEntity(
                    fundCode = "TOUR_UPCOMING_2026",
                    title = "Community Tour - Yercaud 2026",
                    category = "TOUR",
                    description = "Annual family tour to Yercaud hill station",
                    isFixed = true,
                    fixedAmount = 1500.0,
                    startDate = "01-09-2026",
                    status = "Active",
                    connectionType = "SEPARATE",
                    totalCollected = 82500.0,
                    collectionRate = 55
                )
            )
        )

        // 5. Consolidated Community Ledger Entries
        // Matches screenshots: Current Balance ₹5,48,000!
        db.ledgerDao().insertLedgerEntries(
            listOf(
                ConsolidatedLedgerEntity(
                    date = "01-10-2026",
                    details = "Opening Balance Transferred",
                    debit = 0.0,
                    credit = 520000.0,
                    balance = 520000.0,
                    category = "Opening Balance",
                    referenceType = "MANUAL",
                    referenceId = "OP-2026-10"
                ),
                ConsolidatedLedgerEntity(
                    date = "05-10-2026",
                    details = "Hall Rent & Sound System",
                    debit = 1500.0,
                    credit = 0.0,
                    balance = 518500.0,
                    category = "Expense",
                    referenceType = "EXPENSE",
                    referenceId = "EXP-001"
                ),
                ConsolidatedLedgerEntity(
                    date = "08-10-2026",
                    details = "Monthly Collection (90 Members)",
                    debit = 0.0,
                    credit = 9000.0,
                    balance = 527500.0,
                    category = "Collection",
                    referenceType = "COLLECTION",
                    referenceId = "COLL-10-2026"
                ),
                ConsolidatedLedgerEntity(
                    date = "08-10-2026",
                    details = "Savings Fund Collection (60 Members)",
                    debit = 0.0,
                    credit = 30000.0,
                    balance = 557500.0,
                    category = "Savings Fund",
                    referenceType = "COLLECTION",
                    referenceId = "SAV-10-2026"
                ),
                ConsolidatedLedgerEntity(
                    date = "09-10-2026",
                    details = "Printing & Stationeries",
                    debit = 850.0,
                    credit = 0.0,
                    balance = 556650.0,
                    category = "Expense",
                    referenceType = "EXPENSE",
                    referenceId = "EXP-003"
                ),
                ConsolidatedLedgerEntity(
                    date = "09-10-2026",
                    details = "Business Fund Support (M005)",
                    debit = 10000.0,
                    credit = 0.0,
                    balance = 546650.0,
                    category = "Business Fund",
                    referenceType = "BUSINESS_GIVEN",
                    referenceId = "BF-001"
                ),
                ConsolidatedLedgerEntity(
                    date = "10-10-2026",
                    details = "Business Fund Return (M005 Partial)",
                    debit = 0.0,
                    credit = 3000.0,
                    balance = 549650.0,
                    category = "Business Fund",
                    referenceType = "BUSINESS_RETURNED",
                    referenceId = "BFR-001"
                ),
                ConsolidatedLedgerEntity(
                    date = "10-10-2026",
                    details = "Tea & Snacks Meeting Expense",
                    debit = 1650.0,
                    credit = 0.0,
                    balance = 548000.0, // Matches exact ₹5,48,000 Community Fund Balance!
                    category = "Expense",
                    referenceType = "EXPENSE",
                    referenceId = "EXP-005"
                )
            )
        )

        // 6. Expenses (matching Image 10)
        db.expenseDao().insertExpenses(
            listOf(
                ExpenseEntity(
                    date = "07-10-2026",
                    category = "Tea & Snacks",
                    amount = 1500.0,
                    description = "Executive committee meeting refreshments",
                    paymentMethod = "Cash",
                    notes = "Monthly Meeting"
                ),
                ExpenseEntity(
                    date = "10-10-2026",
                    category = "Hall Rent",
                    amount = 3000.0,
                    description = "Devangar Hall booking advance for meeting",
                    paymentMethod = "Bank Transfer",
                    notes = "Community Hall"
                ),
                ExpenseEntity(
                    date = "12-10-2026",
                    category = "Printing",
                    amount = 850.0,
                    description = "Annual report notice booklets and receipts",
                    paymentMethod = "UPI",
                    notes = "Sri Balaji Printers"
                ),
                ExpenseEntity(
                    date = "15-10-2026",
                    category = "Decoration",
                    amount = 2500.0,
                    description = "Deepavali flower garlands and lighting",
                    paymentMethod = "Cash",
                    notes = "Temple festival decor"
                ),
                ExpenseEntity(
                    date = "18-10-2026",
                    category = "Refreshments",
                    amount = 1200.0,
                    description = "Juice and snacks for youth attendees",
                    paymentMethod = "UPI",
                    notes = "Youth wing meet"
                )
            )
        )

        // 7. Chit Funds (matching Image 20 & 22)
        val chit1Id = db.chitDao().insertChitFund(
            ChitFundEntity(
                chitName = "Devangar Gold Chit 2026",
                chitValue = 100000.0,
                memberCount = 20,
                durationMonths = 20,
                monthlyDue = 5000.0,
                commissionPercent = 5.0,
                startDate = "10-06-2026",
                auctionDay = "10th of every month",
                status = "Bidding Open",
                currentCycleMonth = 4,
                notes = "Pre-check verified with Devangar Mahajan guidelines"
            )
        )

        db.chitDao().insertAuction(
            ChitAuctionEntity(
                chitId = chit1Id,
                cycleMonth = 4,
                auctionDate = "10-10-2026",
                winningBidAmount = 15000.0,
                winningMemberId = "M005",
                winningMemberName = "Balaji V",
                communityCommission = 5000.0,
                memberDividend = 10000.0,
                perMemberBenefit = 500.0,
                finalMonthlyDue = 4500.0,
                totalExpectedCollection = 90000.0,
                winnerPayout = 85000.0,
                isConfirmed = true,
                isPayoutRecorded = false
            )
        )

        val chit2Id = db.chitDao().insertChitFund(
            ChitFundEntity(
                chitName = "Monthly Welfare Chit 2026",
                chitValue = 50000.0,
                memberCount = 20,
                durationMonths = 20,
                monthlyDue = 2500.0,
                commissionPercent = 5.0,
                startDate = "10-02-2026",
                auctionDay = "10th of every month",
                status = "Active",
                currentCycleMonth = 8,
                notes = "Small welfare chit"
            )
        )

        db.chitDao().insertAuction(
            ChitAuctionEntity(
                chitId = chit2Id,
                cycleMonth = 8,
                auctionDate = "10-09-2026",
                winningBidAmount = 8000.0,
                winningMemberId = "M012",
                winningMemberName = "S. Murugan",
                communityCommission = 2500.0,
                memberDividend = 5500.0,
                perMemberBenefit = 275.0,
                finalMonthlyDue = 2225.0,
                totalExpectedCollection = 44500.0,
                winnerPayout = 42000.0,
                isConfirmed = true,
                isPayoutRecorded = true
            )
        )

        db.chitDao().insertChitFund(
            ChitFundEntity(
                chitName = "Youth Business Chit",
                chitValue = 200000.0,
                memberCount = 20,
                durationMonths = 20,
                monthlyDue = 10000.0,
                commissionPercent = 5.0,
                startDate = "10-08-2026",
                auctionDay = "10th of every month",
                status = "Active",
                currentCycleMonth = 2,
                notes = "Youth enterprise funding"
            )
        )

        // 8. Member Loans
        val loan1Id = db.loanDao().insertLoan(
            MemberLoanEntity(
                memberId = "M003",
                principalAmount = 25000.0,
                outstandingPrincipal = 18000.0,
                loanDate = "15-05-2026",
                purpose = "Loom machine repair and thread procurement",
                monthlyInterestRate = 1.5,
                status = "Active",
                notes = "Promissory note secured"
            )
        )

        db.loanDao().insertRepayment(
            LoanRepaymentEntity(
                loanId = loan1Id,
                repaymentDate = "15-09-2026",
                totalPayment = 7375.0,
                interestPaid = 375.0,
                principalPaid = 7000.0,
                remainingPrincipal = 18000.0,
                monthYear = "09-2026",
                notes = "Part principal repaid"
            )
        )

        db.loanDao().insertLoan(
            MemberLoanEntity(
                memberId = "M007",
                principalAmount = 30000.0,
                outstandingPrincipal = 30000.0,
                loanDate = "01-08-2026",
                purpose = "Children college admission fee assistance",
                monthlyInterestRate = 1.5,
                status = "Active",
                notes = "Approved in Aug monthly meeting"
            )
        )

        // 9. Business Fund (Interest-free assistance)
        val bfId = db.businessFundDao().insertBusinessFund(
            BusinessFundEntity(
                memberId = "M005",
                amountGiven = 10000.0,
                amountReturned = 5000.0,
                dateGiven = "01-09-2026",
                purpose = "Textile yarn wholesale purchase",
                notes = "Interest-free business emergency revolving fund",
                status = "Active"
            )
        )

        db.businessFundDao().insertReturn(
            BusinessFundReturnEntity(
                businessFundId = bfId,
                returnDate = "20-09-2026",
                returnAmount = 3000.0,
                notes = "First installment returned"
            )
        )

        db.businessFundDao().insertReturn(
            BusinessFundReturnEntity(
                businessFundId = bfId,
                returnDate = "10-10-2026",
                returnAmount = 2000.0,
                notes = "Second installment returned"
            )
        )

        // 10. Festival Gift Distribution (Image 12)
        val distId = db.giftDao().insertDistribution(
            GiftDistributionEntity(
                festivalName = "Festival Gift - Deepavali 2026",
                year = 2026,
                giftType = "CASH",
                cashAmount = 1000.0,
                giftItemName = "Traditional Sweet Box + Cash",
                notes = "Vellore Devangar Community Deepavali Anbalippu",
                totalEligible = 150,
                totalGiven = 120,
                totalPending = 30
            )
        )

        val recipients = mutableListOf<GiftRecipientEntity>()
        memberList.forEachIndexed { idx, m ->
            val isPending = (idx in listOf(2, 4, 15, 23, 31, 44, 52, 60, 71, 79, 85, 93, 101, 109, 117, 125, 133, 141, 148, 149) || idx % 5 == 2)
            val isGiven = !isPending && idx < 125
            recipients.add(
                GiftRecipientEntity(
                    distributionId = distId,
                    memberId = m.memberId,
                    status = if (isGiven) "GIVEN" else "PENDING",
                    dateGiven = if (isGiven) "01-11-2026" else ""
                )
            )
        }
        db.giftDao().insertRecipients(recipients)

        // 11. Meetings
        db.meetingDao().insertMeeting(
            MeetingEntity(
                title = "October General Body Meeting",
                meetingDate = "12-10-2026",
                meetingTime = "10:00 AM",
                venue = "Devangar Community Hall, Gandhi Road, Thorapadi, Vellore",
                agenda = "1. Financial Ledger Audit & Approvals\n2. Deepavali Gift Distribution preparations\n3. Chit Auction Cycle 4 conduct\n4. New member admissions",
                notes = "All members requested to be seated by 9:45 AM",
                isCompleted = false
            )
        )

        // 12. Community Gallery Items
        db.galleryDao().insertGalleryItems(
            listOf(
                GalleryItemEntity(
                    title = "Deepavali Thiruvizha Celebration 2025",
                    category = "Festivals",
                    date = "12-11-2025",
                    photoUrl = "https://images.unsplash.com/photo-1605371924599-2d0365da1ae0?auto=format&fit=crop&w=600&q=80",
                    caption = "Devangar community families celebrating Deepavali at community hall"
                ),
                GalleryItemEntity(
                    title = "Yercaud Community Tour 2025",
                    category = "Tours",
                    date = "15-08-2025",
                    photoUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80",
                    caption = "Group photo of 65 community members during annual tour"
                ),
                GalleryItemEntity(
                    title = "General Body Meeting & Accounts Presentation",
                    category = "Meetings",
                    date = "14-09-2026",
                    photoUrl = "https://images.unsplash.com/photo-1511578314322-379afb476865?auto=format&fit=crop&w=600&q=80",
                    caption = "Office bearers presenting audited ledgers to community elders"
                )
            )
        )

        // 13. Settings
        db.settingsDao().insertOrUpdateSettings(
            CommunitySettingsEntity()
        )
        }
    }
}
