use strict;

use XML::Twig;

#my $t = XML::Twig->new( twig_handlers => { row => \&row, } );
my $t = XML::Twig->new( twig_handlers => { 'users/row' => \&row } );
$t->parsefile('/media/Stock/home/users/ponza/stack_overflow_201208/posthistory_noC.xml');

$t->purge;    # don't forget to flush one last time in the end or anything

# after the last </section> tag will not be output

# the handler is called once a section is completely parsed, ie when
# the end tag for section is found, it receives the twig itself and
# the element (including all its sub-elements) as arguments
sub row {
	my ( $t, $row ) = @_;    # arguments for all twig_handlers
	      #$section->set_tag('div');    # change the tag name.4, my favourite method...
	      # let's use the attribute nb as a prefix to the title
	      #my $title = $section->first_child('title');    # find the title
	my $id = $row->att('Id');    # get the attribute
	                             #$title->prefix("$nb - ");                      # easy isn't it?
	print $id. "\n";
	$row->purge;                 # outputs the section and frees memory
}
