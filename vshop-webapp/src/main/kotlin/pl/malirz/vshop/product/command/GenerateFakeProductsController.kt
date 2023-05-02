package pl.malirz.vshop.product.command

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Positive
import mu.KotlinLogging
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.malirz.vshop.shared.domain.utils.IdGenerator
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.function.Consumer
import java.util.stream.IntStream
import kotlin.random.Random
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/products/generate")
internal class GenerateFakeProductsController(
    private val handler: AddProductCommandHandler, private val idGenerator: IdGenerator
) : Consumer<GenerateProductsRequest> {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/{numberOfProducts}")
    override fun accept(@ModelAttribute @Valid generateProductsRequest: GenerateProductsRequest) {
        val numberOfProducts = generateProductsRequest.numberOfProducts

        val addProductCommands = mutableListOf<AddProductCommand>()

        IntStream.rangeClosed(1, numberOfProducts).forEach {
            val selectedWords = RANDOM_WORDS.shuffled().stream()
                .skip(Random.nextInt(0, 479).toLong()).limit(20).toList()

            val addProductCommand = AddProductCommand(
                id = idGenerator.generate(),
                code = it.toString(),
                name = selectedWords.shuffled().stream().limit(3).toArray().joinToString("-"),
                description = selectedWords.stream().toArray().joinToString(" "),
                price = BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP),
                quantity = (Math.random() * 1000).toInt(),
                revision = 1L
            )
            addProductCommands.add(addProductCommand)
        }

        logger.info { "Start initialization of products ($numberOfProducts products)" }

        val timeInMillis = measureTimeMillis {
            addProductCommands.forEach { handler.accept(it) }
        }

        logger.info { "End initialization of products  ($numberOfProducts products): $timeInMillis ms" }
    }
}

internal data class GenerateProductsRequest(
    @field:Positive
    @field:Max(1_000_000)
    val numberOfProducts: Int
)

val RANDOM_WORDS = listOf(
    "lubrication",
    "nonelectrolyte",
    "toonie",
    "dimerous",
    "tramped",
    "tidiest",
    "nouvelles",
    "overboils",
    "smouldering",
    "botflies",
    "volvuli",
    "emeroids",
    "forkinesses",
    "externships",
    "marcelling",
    "scyphus",
    "absterged",
    "deinonychuses",
    "woodbins",
    "cancellable",
    "phreakers",
    "depends",
    "overbids",
    "estrogenic",
    "replacement",
    "blader",
    "expletive",
    "oddish",
    "messy",
    "seneschal",
    "embolus",
    "communality",
    "disbursals",
    "hermeticisms",
    "cosmographic",
    "buddying",
    "vulval",
    "fatted",
    "endurably",
    "cesarians",
    "euthanize",
    "aponeurosis",
    "bipartitely",
    "headrests",
    "upperparts",
    "reinformed",
    "whangees",
    "osetra",
    "verities",
    "governmentese",
    "recounts",
    "cataclysmal",
    "parietal",
    "gagsters",
    "grape",
    "capitulations",
    "humanism",
    "gentian",
    "jarrah",
    "bullrushes",
    "dogears",
    "pyroxylins",
    "lupines",
    "depositor",
    "superhelical",
    "percentile",
    "chuddahs",
    "whaleboat",
    "karyotins",
    "gimpiest",
    "boshboks",
    "breathlessness",
    "pachisis",
    "odometry",
    "redial",
    "octillions",
    "concomitances",
    "determinants",
    "exposers",
    "territorialized",
    "frames",
    "oligocene",
    "prides",
    "compositions",
    "snowbelts",
    "aerobats",
    "seriately",
    "subtractions",
    "bipack",
    "fleshy",
    "picturized",
    "zigzagging",
    "oilways",
    "trivializing",
    "bulled",
    "analogists",
    "mazers",
    "chincherinchees",
    "considerately",
    "balefires",
    "depreciators",
    "bemedalled",
    "kneeholes",
    "companionably",
    "recompensing",
    "quested",
    "romeldale",
    "permissively",
    "noncombatants",
    "proboscises",
    "tertiary",
    "tranquility",
    "cancellous",
    "vibe",
    "heather",
    "heterogenies",
    "welcome",
    "bonnet",
    "multistemmed",
    "snarkily",
    "externe",
    "overnutrition",
    "reemphasize",
    "dungier",
    "optionalities",
    "gaun",
    "subproject",
    "derrick",
    "placekicked",
    "sectarianize",
    "cans",
    "luteinize",
    "roothold",
    "deluxe",
    "cullied",
    "whensoever",
    "lollypops",
    "minefields",
    "bullet",
    "glazers",
    "sarongs",
    "prosaists",
    "stokesia",
    "bawdries",
    "mucoprotein",
    "unsteeled",
    "thiocyanates",
    "crackhead",
    "corporeities",
    "systoles",
    "avocational",
    "originalities",
    "remex",
    "mooed",
    "afforest",
    "deployment",
    "sinapisms",
    "unaffiliated",
    "removed",
    "ingrownnesses",
    "zealous",
    "calls",
    "toughening",
    "yows",
    "fumatory",
    "undergoing",
    "icecapped",
    "unexercised",
    "stative",
    "hulloing",
    "triunity",
    "steamered",
    "counterstrike",
    "assonantal",
    "spokeshaves",
    "deplorably",
    "yoked",
    "laxer",
    "tooler",
    "ladypalm",
    "symptomatically",
    "prototypal",
    "promethiums",
    "guru",
    "indeterminisms",
    "piggishness",
    "dowsabel",
    "betaken",
    "widdie",
    "sith",
    "subcomponent",
    "reconciliations",
    "oxidases",
    "isochrone",
    "hotnesses",
    "teeterboards",
    "moralistic",
    "absorptivities",
    "sauropods",
    "deservers",
    "inexpediency",
    "nymphets",
    "exam",
    "menazon",
    "reduplicate",
    "rifler",
    "pingrasses",
    "dissipating",
    "immensely",
    "hardhats",
    "pseudocyeses",
    "heterogamete",
    "leucomas",
    "silages",
    "reactivating",
    "rights",
    "mannered",
    "overprogramming",
    "ecchymoses",
    "moor",
    "obstructionist",
    "telegramming",
    "invalidated",
    "litotes",
    "dicoumarols",
    "phagosome",
    "reenergizing",
    "chlorophylls",
    "colleges",
    "popeyed",
    "griskins",
    "synchronies",
    "dottiness",
    "nictated",
    "unwilled",
    "eventuated",
    "move",
    "hirsled",
    "stoures",
    "populisms",
    "derepress",
    "newsletters",
    "cerveza",
    "dither",
    "unshipped",
    "ogle",
    "slowpoke",
    "covertness",
    "greedsome",
    "bluecaps",
    "pocketknife",
    "eustele",
    "polarizations",
    "thermals",
    "consorting",
    "calcifugous",
    "sweeten",
    "bailsman",
    "woodenhead",
    "misbegin",
    "ovolo",
    "factious",
    "tantalums",
    "reprehensible",
    "hypersonic",
    "bind",
    "cauteries",
    "colleague",
    "cineastes",
    "mistrained",
    "boonies",
    "valuate",
    "shinnying",
    "redivision",
    "conceit",
    "trois",
    "lamsters",
    "lense",
    "buggery",
    "orbitals",
    "snarfs",
    "shorthanded",
    "whoreson",
    "swiss",
    "aitchbones",
    "suspension",
    "insinuatingly",
    "breezinesses",
    "pyrrhics",
    "diaster",
    "immunogens",
    "vaccinating",
    "vizslas",
    "diuretic",
    "densifies",
    "aftertax",
    "neologistic",
    "phoneys",
    "contractible",
    "apsidal",
    "overdrafts",
    "repossessions",
    "barnacle",
    "invalidities",
    "trichology",
    "thatchy",
    "offsides",
    "chiltepin",
    "gumminesses",
    "eager",
    "deicides",
    "robotism",
    "nitrid",
    "tamable",
    "solfataras",
    "epochally",
    "tassel",
    "lifer",
    "commenting",
    "inhumanity",
    "immunoassayable",
    "governorships",
    "startlement",
    "polyamides",
    "equestrienne",
    "mobilised",
    "oncogenesis",
    "leprosariums",
    "sesquiterpene",
    "waterscapes",
    "centromere",
    "segetal",
    "blindsiding",
    "wavery",
    "postamputation",
    "chibouk",
    "specks",
    "paregorics",
    "backstabbed",
    "reembroidered",
    "fluvial",
    "vasopressin",
    "moola",
    "phosphatizing",
    "supercar",
    "diagnose",
    "defoliant",
    "stomps",
    "wheel",
    "wham",
    "rec",
    "intoning",
    "bicarb",
    "proved",
    "tumors",
    "parasitological",
    "slues",
    "bottomers",
    "ascends",
    "azulejos",
    "sociocultural",
    "mirador",
    "perique",
    "quicksteps",
    "plashed",
    "neurulae",
    "infectant",
    "sheal",
    "populaces",
    "annoys",
    "coendures",
    "interrelations",
    "detoxifies",
    "enroot",
    "amativenesses",
    "actualizes",
    "lapidarian",
    "sheughs",
    "theorbos",
    "ironings",
    "exiting",
    "numerary",
    "indigene",
    "safranine",
    "awless",
    "dukedoms",
    "speedinesses",
    "factorization",
    "priapism",
    "amblygonite",
    "cowries",
    "sphenoidal",
    "cares",
    "amidins",
    "wafts",
    "orts",
    "mayos",
    "agarics",
    "thoroughbreds",
    "buttonhooking"
)


